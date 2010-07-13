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
package org.madogiwa.plaintable.dialect;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.madogiwa.plaintable.util.JdbcUtils;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class DialectFactory {

	private DataSource dataSource;

	public DialectFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Dialect getDialect() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String productName = connection.getMetaData()
					.getDatabaseProductName();
			if (productName.startsWith("MySQL")) {
				return new MySQLDialect();
			} else if (productName.startsWith("PostgreSQL")) {
				return new PostgreSQLDialect();
			} else if (productName.startsWith("Apache Derby")) {
				return new DerbyDialect();
			} else if (productName.startsWith("H2")) {
				return new H2Dialect();
			} else {
				throw new RuntimeException();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeConnection(connection);
		}
	}

}
