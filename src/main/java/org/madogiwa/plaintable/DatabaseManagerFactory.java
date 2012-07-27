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
package org.madogiwa.plaintable;

import org.madogiwa.plaintable.dialect.Dialect;
import org.madogiwa.plaintable.dialect.DialectFactory;
import org.madogiwa.plaintable.impl.DatabaseManagerImpl;

import javax.sql.DataSource;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class DatabaseManagerFactory {

	private DataSource dataSource;

	private String prefix = "";

	/**
	 * @param dataSource
	 */
	public DatabaseManagerFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return
	 */
	public DatabaseManager getDatabaseManager() {
		DialectFactory dialectFactory = new DialectFactory(dataSource);
		Dialect dialect = dialectFactory.getDialect();

		return new DatabaseManagerImpl(dataSource, dialect, prefix);
	}

}
