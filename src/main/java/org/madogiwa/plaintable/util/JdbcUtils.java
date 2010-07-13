/*
 * Copyright (c) 2008 Hidenori Sugiyama
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
package org.madogiwa.plaintable.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class JdbcUtils {

	/**
	 * @param connection
	 */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * @param statement
	 */
	public static void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * @param resultSet
	 */
	public static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * @param dataSource
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static boolean isTableExist(DataSource dataSource, String tableName)
			throws SQLException {
		Connection connection = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			resultSet = metaData.getTables(connection.getCatalog(), null,
					tableName, new String[] { "TABLE" });
			if (resultSet.next()) {
				return true;
			}
			resultSet.close();

			resultSet = metaData.getTables(connection.getCatalog(), null,
					tableName.toLowerCase(), new String[] { "TABLE" });
			if (resultSet.next()) {
				return true;
			}
			resultSet.close();

			resultSet = metaData.getTables(connection.getCatalog(), null,
					tableName.toUpperCase(), new String[] { "TABLE" });
			if (resultSet.next()) {
				return true;
			}

			return false;
		} finally {
			JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeConnection(connection);
		}
	}

	/**
	 * @param dataSource
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int executeUpdate(DataSource dataSource, String sql,
			Object[] params) throws SQLException {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			return executeUpdate(connection, sql, params);
		} finally {
			closeConnection(connection);
		}
	}

	/**
	 * @param connection
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int executeUpdate(Connection connection, String sql,
			Object[] params) throws SQLException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			return statement.executeUpdate();
		} finally {
			JdbcUtils.closeStatement(statement);
		}
	}

}
