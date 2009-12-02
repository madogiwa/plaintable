/*
 * Copyright (c) 2009 Hidenori Sugiyama
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 */
package org.madogiwa.plaintable.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.madogiwa.plaintable.DatabaseSchema;
import org.madogiwa.plaintable.dialect.Dialect;
import org.madogiwa.plaintable.schema.AttributeColumn;
import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.ReferenceKey;
import org.madogiwa.plaintable.schema.Schema;
import org.madogiwa.plaintable.schema.SchemaReference;
import org.madogiwa.plaintable.schema.SyntheticKey;
import org.madogiwa.plaintable.schema.attr.BytesAttribute;
import org.madogiwa.plaintable.schema.attr.LongAttribute;
import org.madogiwa.plaintable.schema.attr.StringAttribute;
import org.madogiwa.plaintable.util.JdbcUtils;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Hidenori Sugiyama
 *
 */
public class DatabaseSchemaImpl implements DatabaseSchema {

	public static final String LOCK_TABLE = "PlaintableLock";

	private static final String SCHEMA_TABLE = "PlaintableSchema";

	private static Logger logger = Logger.getLogger(DatabaseSchemaImpl.class.getName());

	private DataSource dataSource;

	private Dialect dialect;

	private Connection connection;

	private boolean initialized;

	/**
	 * @param dataSource
	 * @param dialect
	 */
	public DatabaseSchemaImpl(DataSource dataSource, Dialect dialect) {
		this.dataSource = dataSource;
		this.dialect = dialect;
		this.initialized = false;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.DatabaseSchema#open()
	 */
	public void open() throws SQLException {
		connection = dataSource.getConnection();
		connection.setAutoCommit(false);

		if (!initialized) {
			init();
			initialized = true;
		}

		lock();
	}

	/**
	 * @throws SQLException 
	 * 
	 */
	private void init() throws SQLException {
		if (!JdbcUtils.isTableExist(dataSource, LOCK_TABLE)) {
			initLockTable();
		}
		if (!JdbcUtils.isTableExist(dataSource, SCHEMA_TABLE)) {
			initSchemaTable();
		}
	}

	/**
	 * @throws SQLException 
	 * 
	 */
	private void initLockTable() throws SQLException {
		Schema schema = getLockTableSchema();
		String sql = buildCreateTableSql(schema);
		JdbcUtils.executeUpdate(connection, sql, new Object[] {});
		connection.commit();
	}

	/**
	 * @throws SQLException 
	 * 
	 */
	private void initSchemaTable() throws SQLException {
		Schema schema = getPlaintableSchema();
		String sql = buildCreateTableSql(schema);
		JdbcUtils.executeUpdate(connection, sql, new Object[] {});
		addLock(schema);
		connection.commit();
	}

	/**
	 * @throws SQLException
	 */
	private void lock() throws SQLException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(String.format(
					"SELECT * FROM %s WHERE name = ? FOR UPDATE", LOCK_TABLE));
			statement.setString(1, SCHEMA_TABLE);
			statement.execute();
		} finally {
			JdbcUtils.closeStatement(statement);
		}
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.DatabaseSchema#close()
	 */
	public void close() throws SQLException {
		connection.commit();
		JdbcUtils.closeConnection(connection);
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.DatabaseManager#createTable(org.madogiwa.plaintable.schema.Schema)
	 */
	public void createTable(Schema schema) throws SQLException {
		String sql = buildCreateTableSql(schema);
		JdbcUtils.executeUpdate(connection, sql, new Object[] {});

		createIndicies(schema);
		addSchema(schema);
		addLock(schema);
	}

	/**
	 * @param schema
	 * @return
	 */
	private String buildCreateTableSql(Schema schema) {
		StringBuilder query = new StringBuilder();
		query.append(String.format("CREATE TABLE %s ( ", dialect.quote(schema.getName())));
		query.append(String.format("%s %s PRIMARY KEY ", dialect.quote(schema.getSyntheticKey().getName()), dialect.getSQLType(SyntheticKey.class, -1)));

		for(ReferenceKey ref : schema.getReferenceKeys()) {
			query.append(String.format(", %s %s ", dialect.quote(ref.getName()), dialect.getSQLType(ReferenceKey.class, -1)));
			query.append( ref.isNullable() ? "" : " NOT NULL " );
			query.append( ref.isUnique() ? " UNIQUE " : "" );

			SchemaReference schemaRef = ref.getTarget();
			query.append(String.format(" REFERENCES %s(%s) ", dialect.quote(schemaRef.getSchema().getName()), dialect.quote(schemaRef.getSchema().getSyntheticKey().getName())));
			query.append( ref.isCascade() ? " ON DELETE CASCADE " : "" );
		}

		for(AttributeColumn attr : schema.getAttributes()) {
			String type = dialect.getSQLType(attr.getClass(), attr.getLength());
			query.append(String.format(", %s %s", dialect.quote(attr.getName()), type));
			query.append( attr.isNullable() ? "" : " NOT NULL " );
			query.append( attr.isUnique() ? " UNIQUE " : "" );
		}

		query.append(" )");
		System.out.print(query);
		return query.toString();
	}

	/**
	 * @param schema
	 * @throws SQLException
	 */
	private void createIndicies(Schema schema) throws SQLException {
		List<Column> indexedColumns = new ArrayList<Column>();

		for(ReferenceKey ref : schema.getReferenceKeys()) {
			if (ref.isIndexed()) {
				indexedColumns.add(ref);
			}
		}
		for(AttributeColumn attr : schema.getAttributes()) {
			if (attr.isIndexed()) {
				indexedColumns.add(attr);
			}
		}

		for(Column column : indexedColumns) {
			if (!column.isUnique()) {
				JdbcUtils.executeUpdate(connection, String.format("CREATE %s INDEX %s ON %s (%s)", column.isUnique() ? "UNIQUE" : "", schema.getName() + "_" + column.getName(), schema.getName(), column.getName()), new Object[] {});  
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.DatabaseManager#dropTable(org.madogiwa.plaintable.schema.Schema)
	 */
	public void dropTable(Schema schema) throws SQLException {
		StringBuilder query = new StringBuilder();
		query.append(String.format("DROP TABLE %s", dialect.quote(schema.getName())));
		JdbcUtils.executeUpdate(connection, query.toString(), new Object[] {});

		removeLock(schema.getName());
		removeSchema(schema.getName());
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.DatabaseManager#retrieveSchema(java.lang.String)
	 */
	public Schema retrieveSchema(String name) throws SQLException {
		return retrieveSchemaMap().get(name);
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.DatabaseSchema#retrieveSchemaMap()
	 */
	public Map<String, Schema> retrieveSchemaMap() throws SQLException {
		Map<String, Schema> map = new HashMap<String, Schema>();

		String query = String.format("SELECT %s,%s,%s FROM %s", dialect.quote("name"), dialect.quote("version"), dialect.quote("schema"), dialect.quote(SCHEMA_TABLE));
		logger.fine(query);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query.toString());
			resultSet = statement.executeQuery();
			while(resultSet.next()) {
				byte[] schemaBytes = resultSet.getBytes("schema");
				Schema schema = schemaFromByteArray(schemaBytes);
				map.put(schema.getName(), schema);
			}
		} finally {
			JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeStatement(statement);
			JdbcUtils.closeConnection(connection);
		}
		return map;
	}

	private Schema schemaFromByteArray(byte[] schemaBytes) {
		try {
			ByteArrayInputStream bytesIn = new ByteArrayInputStream(schemaBytes);
			ObjectInputStream objIn = new ObjectInputStream(bytesIn);
			Schema schema = (Schema) objIn.readObject();
			objIn.close();
			return schema;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.DatabaseManager#updateTable(org.madogiwa.plaintable.schema.Schema)
	 */
	public void updateTable(Schema schema) throws SQLException {
		throw new NotImplementedException();
	}

	/**
	 * @return
	 */
	private Schema getPlaintableSchema() {
		Schema schema = new Schema(SCHEMA_TABLE, 1);
		schema.setSyntheticKey(new SyntheticKey(schema, "id"));
		schema.addAttribute(new StringAttribute(schema, "name"));
		schema.addAttribute(new LongAttribute(schema, "version"));
		schema.addAttribute(new BytesAttribute(schema, "schema"));
		return schema;
	}

	/**
	 * @return
	 */
	private Schema getLockTableSchema() {
		Schema schema = new Schema(LOCK_TABLE, 1);
		schema.setSyntheticKey(new SyntheticKey(schema, "id"));
		schema.addAttribute(new StringAttribute(schema, "name"));
		return schema;
	}

	private void addSchema(Schema schema) throws SQLException {
		JdbcUtils.executeUpdate(connection, String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", SCHEMA_TABLE, dialect.quote("name"), dialect.quote("version"), dialect.quote("schema")), new Object[] {schema.getName(), schema.getVersion(), schemaToByteArray(schema)});
	}

	private byte[] schemaToByteArray(Schema schema) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
			objOut.writeObject(schema);
			objOut.close();
			return byteOut.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void removeSchema(String name) throws SQLException {
		JdbcUtils.executeUpdate(connection, String.format("DELETE FROM %s WHERE name = ?", SCHEMA_TABLE), new Object[] {name});
	}

	private void addLock(Schema schema) throws SQLException {
		JdbcUtils.executeUpdate(connection, String.format("INSERT INTO %s (name) VALUES (?)", LOCK_TABLE), new Object[] {schema.getName()});
	}

	private void removeLock(String name) throws SQLException {
		JdbcUtils.executeUpdate(connection, String.format("DELETE FROM %s WHERE name = ?", LOCK_TABLE), new Object[] {name});
	}

}
