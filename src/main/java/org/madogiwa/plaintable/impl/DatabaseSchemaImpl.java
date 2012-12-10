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

import org.madogiwa.plaintable.DatabaseSchema;
import org.madogiwa.plaintable.dialect.Dialect;
import org.madogiwa.plaintable.schema.AttributeColumn;
import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.Index;
import org.madogiwa.plaintable.schema.PrimaryKey;
import org.madogiwa.plaintable.schema.ReferenceKey;
import org.madogiwa.plaintable.schema.Schema;
import org.madogiwa.plaintable.schema.SchemaReference;
import org.madogiwa.plaintable.schema.SyntheticKey;
import org.madogiwa.plaintable.schema.TextAttribute;
import org.madogiwa.plaintable.schema.attr.BignumAttribute;
import org.madogiwa.plaintable.schema.attr.BooleanAttribute;
import org.madogiwa.plaintable.schema.attr.BytesAttribute;
import org.madogiwa.plaintable.schema.attr.CharAttribute;
import org.madogiwa.plaintable.schema.attr.DateAttribute;
import org.madogiwa.plaintable.schema.attr.DoubleAttribute;
import org.madogiwa.plaintable.schema.attr.FloatAttribute;
import org.madogiwa.plaintable.schema.attr.IntegerAttribute;
import org.madogiwa.plaintable.schema.attr.LongAttribute;
import org.madogiwa.plaintable.schema.attr.ShortAttribute;
import org.madogiwa.plaintable.schema.attr.StreamAttribute;
import org.madogiwa.plaintable.schema.attr.StringAttribute;
import org.madogiwa.plaintable.schema.attr.TimeAttribute;
import org.madogiwa.plaintable.schema.attr.TimestampAttribute;
import org.madogiwa.plaintable.util.JdbcUtils;
import org.madogiwa.plaintable.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class DatabaseSchemaImpl implements DatabaseSchema {

	private static Logger logger = Logger.getLogger(DatabaseSchemaImpl.class
			.getName());

	private DataSource dataSource;

	private Dialect dialect;

	private Connection connection;

	private boolean initialized;

	private String prefix;

	/**
	 * @param dataSource
	 * @param dialect
	 */
	public DatabaseSchemaImpl(DataSource dataSource, Dialect dialect, String prefix) {
		this.dataSource = dataSource;
		this.dialect = dialect;
		this.prefix = prefix;
		this.initialized = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.DatabaseSchema#open()
	 */
	public void open() throws SQLException {
		connection = dataSource.getConnection();
		connection.setAutoCommit(false);

		if (!initialized) {
			init();
			initialized = true;
		}
	}

	/**
	 * @throws SQLException
	 * 
	 */
	private void init() throws SQLException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.DatabaseSchema#close()
	 */
	public void close() throws SQLException {
		connection.commit();
		JdbcUtils.closeConnection(connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.DatabaseManager#createTable(org.madogiwa.plaintable
	 * .schema.Schema)
	 */
	public void createTable(Schema schema) throws SQLException {
		String sql = buildCreateTableSql(schema);
		JdbcUtils.executeUpdate(connection, sql, new Object[]{});

		createIndicies(schema);
	}

	/**
	 * @param schema
	 * @return
	 */
	private String buildCreateTableSql(Schema schema) {
		StringBuilder query = new StringBuilder();
		query.append(String.format("CREATE TABLE %s ( ",
				dialect.quote(schema.getFullName())));
		query.append(String.format("%s %s PRIMARY KEY ",
				dialect.quote(schema.getPrimaryKey().getName()),
				dialect.getSQLType(SyntheticKey.class, -1)));

		for (AttributeColumn attr : schema.getAttributes()) {
			String type = dialect.getSQLType(attr.getClass(), attr.getLength());
			query.append(String.format(", %s %s",
					dialect.quote(attr.getName()), type));
			query.append(attr.isNullable() ? "" : " NOT NULL ");
		}

		for (ReferenceKey ref : schema.getReferenceKeys()) {
			query.append(", ");
			query.append(dialect.buildForeignKeyConstraint(ref));
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
		for (Index index : schema.getIndices()) {
			List<String> list = new ArrayList<String>();
			for(Column column : index.getColumns()) {
				list.add(column.getName());
			}

			String indexName = schema.getFullName() + "_" + StringUtils.join(list, "_");
			String columnNameList = StringUtils.join(list, ",");

			JdbcUtils.executeUpdate(connection, String.format(
					"CREATE %s INDEX %s ON %s (%s)",
					index.isUnique() ? "UNIQUE" : "",
					dialect.quote(indexName),
					dialect.quote(schema.getFullName()),
					dialect.quote(columnNameList)), new Object[] {});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.DatabaseManager#dropTable(org.madogiwa.plaintable
	 * .schema.Schema)
	 */
	public void dropTable(Schema schema) throws SQLException {
		logger.info(String.format("drop table %s", schema.getFullName()));

		StringBuilder query = new StringBuilder();
		query.append(String.format("DROP TABLE %s",
				dialect.quote(schema.getFullName())));
		JdbcUtils.executeUpdate(connection, query.toString(), new Object[] {});
	}

	public Map<String, Schema> retrieveSchemaMap() throws SQLException {
		Map<String, Schema> map = new HashMap<String, Schema>();

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();

			String[] tableNames = getTableNames(metaData);
			for(String name : tableNames) {
				logger.fine("exist table %s".format(name));

				Schema schema = getSchema(metaData, name);
				map.put(name, schema);
			}

			resolveSchemaReference(map);
		} finally {
			JdbcUtils.closeConnection(connection);
		}

		return map;
	}

	private void resolveSchemaReference(Map<String, Schema> map) {
		for(Schema schema : map.values()) {
			logger.fine("schema " + schema.getFullName());
			for(ReferenceKey key : schema.getReferenceKeys()) {
				Schema target = map.get(key.getTarget().getSchema().getName());
				logger.fine("reference " + target.getFullName());
				key.setTarget(new SchemaReference(target));
			}
		}
	}

	private String[] getTableNames(DatabaseMetaData metaData) throws SQLException {
		List<String> list = new ArrayList<String>();

		ResultSet resultSet = null;
		try {
			resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
			while(resultSet.next()) {
				String tableName = resultSet.getString("TABLE_NAME");
				// TODO: remove prefix
				list.add(tableName);
			}
		} finally {
			JdbcUtils.closeResultSet(resultSet);
		}

		return list.toArray(new String[]{});
	}

	private Schema getSchema(DatabaseMetaData metaData, String tableName) throws SQLException {
		Schema schema = new Schema(tableName);
		updateReference(metaData, schema);
		updateSchemaColumn(metaData, schema);
		updateSchemaIndex(metaData, schema);
		return schema;
	}

	private void updateSchemaColumn(DatabaseMetaData metaData, Schema schema) throws SQLException {
		List<String> primaryKeys = getPrimaryKeys(metaData, schema);
		Set<ReferenceKey> referenceKeys = schema.getReferenceKeys();

		ResultSet resultSet = null;
		try {
			resultSet = metaData.getColumns(null, null, schema.getFullName(), "%");
			while(resultSet.next()) {
				String columnName = resultSet.getString("COLUMN_NAME");
				int dataType = resultSet.getInt("DATA_TYPE");

				logger.fine(String.format("found %s(%d)", columnName, dataType));

				if (primaryKeys.contains(columnName)) {
					schema.setPrimaryKey(makePrimaryKeyColumn(schema, columnName, resultSet));
				} else if (schema.hasReferenceKey(columnName)) {
					ReferenceKey key = schema.getReferenceKey(columnName);
					String nullable = resultSet.getString("IS_NULLABLE");
					key.setNullable(nullable.equals("YES") ? true : false);
				} else {
					AttributeColumn attr = makeAttributeColumn(dataType, schema, columnName);
					if (attr == null) {
						continue;
					}

					if (attr instanceof TextAttribute) {
						attr.setLength(resultSet.getInt("CHAR_OCTET_LENGTH"));
					} else {
						attr.setLength(resultSet.getInt("COLUMN_SIZE"));
					}

					String nullable = resultSet.getString("IS_NULLABLE");
					attr.setNullable( nullable.equals("YES") ? true : false );

					schema.addAttribute(attr);
				}
			}
		} finally {
			JdbcUtils.closeResultSet(resultSet);
		}
	}

	private List<String> getPrimaryKeys(DatabaseMetaData metaData, Schema schema) throws SQLException {
		List<String> list = new ArrayList<String>();

		ResultSet resultSet = null;
		try {
			resultSet = metaData.getPrimaryKeys(null, null, schema.getFullName());
			while(resultSet.next()) {
				list.add(resultSet.getString("COLUMN_NAME"));
			}
		} finally {
			JdbcUtils.closeResultSet(resultSet);
		}

		return list;
	}

	private PrimaryKey makePrimaryKeyColumn(Schema schema, String columnName, ResultSet resultSet) throws SQLException {
		PrimaryKey key = new SyntheticKey(schema, columnName);
		return key;
	}

	private AttributeColumn makeAttributeColumn(int dataType, Schema schema, String columnName) {
		AttributeColumn attr = null;

		switch(dataType) {
			case Types.BOOLEAN:
				attr = new BooleanAttribute(schema, columnName);
				break;
			case Types.SMALLINT:
				attr = new ShortAttribute(schema, columnName);
				break;
			case Types.INTEGER:
				attr = new IntegerAttribute(schema, columnName);
				break;
			case Types.BIGINT:
				attr = new LongAttribute(schema, columnName);
				break;
			case Types.DECIMAL:
				attr = new BignumAttribute(schema, columnName);
				break;
			case Types.REAL:
				attr = new FloatAttribute(schema, columnName);
				break;
			case Types.FLOAT:
				attr = new FloatAttribute(schema, columnName);
				break;
			case Types.DOUBLE:
				attr = new DoubleAttribute(schema, columnName);
				break;
			case Types.TIME:
				attr = new TimeAttribute(schema, columnName);
				break;
			case Types.DATE:
				attr = new DateAttribute(schema, columnName);
				break;
			case Types.TIMESTAMP:
				attr = new TimestampAttribute(schema, columnName);
				break;
            case Types.CHAR:
                attr = new CharAttribute(schema, columnName);
                break;
			case Types.VARCHAR:
				attr = new CharAttribute(schema, columnName);
				break;
			case Types.VARBINARY:
				attr = new BytesAttribute(schema, columnName);
				break;
			case Types.CLOB:
				attr = new StringAttribute(schema, columnName);
				break;
			case Types.BLOB:
				attr = new StreamAttribute(schema, columnName);
				break;
			case Types.LONGVARCHAR:
				attr = new StringAttribute(schema, columnName);
				break;
			default:
				logger.warning(String.format("table %s has unknown column type %s(%d)", schema.getName(), columnName, dataType));
				break;
		}

		return attr;
	}

	private void updateSchemaIndex(DatabaseMetaData metaData, Schema schema) throws SQLException {
		// TODO: support multiple column
		ResultSet resultSet = null;
		try {
			resultSet = metaData.getIndexInfo(null, null, schema.getFullName(), false, false);
			while(resultSet.next()) {
				String columnName = resultSet.getString("COLUMN_NAME");
				if (columnName == null) {
					continue;
				}

				String indexName = resultSet.getString("INDEX_NAME");

				Column column = schema.getAttribute(columnName);
				if (column == null) {
					column = schema.getReferenceKey(columnName);
				}

				if (column == null) {
					logger.fine(String.format("%s is not attribute name", columnName));
					continue;
				}

				Index index = new Index(schema, column);
				index.setUnique(!resultSet.getBoolean("NON_UNIQUE"));

				schema.addIndex(index);
			}
		} finally {
			JdbcUtils.closeResultSet(resultSet);
		}
	}

	private void updateReference(DatabaseMetaData metaData, Schema schema) throws SQLException {
		ResultSet resultSet = null;
		try {
			logger.fine(String.format("find %s reference", schema.getFullName()));
			resultSet = metaData.getImportedKeys(null, null, schema.getFullName());
			while(resultSet.next()) {
				String columnName = resultSet.getString("FKCOLUMN_NAME");
				String tableName = resultSet.getString("PKTABLE_NAME");

				logger.fine(String.format("%s : %s", columnName, tableName));

				Schema dummy = new Schema(tableName);
				SchemaReference ref = new SchemaReference(dummy);
				ReferenceKey key = new ReferenceKey(schema, columnName, ref);

				// cascade
				// resultSet.getShort("UPDATE_RULE");
				// resultSet.getShort("DELETE_RULE");

				schema.addReferenceKey(key);
			}
		} finally {
			JdbcUtils.closeResultSet(resultSet);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.DatabaseManager#updateTable(org.madogiwa.plaintable
	 * .schema.Schema)
	 */
	public void updateTable(Schema schema) throws SQLException {
		throw new UnsupportedOperationException();
	}

}
