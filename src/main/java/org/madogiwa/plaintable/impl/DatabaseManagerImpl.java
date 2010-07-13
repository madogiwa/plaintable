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

import javax.sql.DataSource;

import org.madogiwa.plaintable.DatabaseManager;
import org.madogiwa.plaintable.DatabaseSchema;
import org.madogiwa.plaintable.SchemaManager;
import org.madogiwa.plaintable.Session;
import org.madogiwa.plaintable.dialect.Dialect;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class DatabaseManagerImpl implements DatabaseManager {

	private DataSource dataSource;

	private Dialect dialect;

	private DatabaseSchema databaseSchema;

	private SchemaManager schemaManager;

	/**
	 * @param dataSource
	 * @param dialect
	 */
	public DatabaseManagerImpl(DataSource dataSource, Dialect dialect) {
		this.dataSource = dataSource;
		this.dialect = dialect;

		databaseSchema = new DatabaseSchemaImpl(dataSource, dialect);
		schemaManager = new SchemaManagerImpl(databaseSchema);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.DatabaseManager#getSchemaManager()
	 */
	public SchemaManager getSchemaManager() {
		return schemaManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.DatabaseManager#newSession()
	 */
	public Session newSession() {
		return new SessionImpl(dataSource, dialect);
	}

}
