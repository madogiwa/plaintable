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
package org.madogiwa.plaintable;

import org.madogiwa.plaintable.schema.Schema;
import org.madogiwa.plaintable.schema.SchemaDefinition;

/**
 * @author Hidenori Sugiyama
 * 
 */
public interface SchemaManager {

	enum SynchronizeMode {
		ALL_DROP_AND_CREATE, DROP_AND_CREATE, UPDATE_ONLY, NONE, CHECK_COMPATIBILITY,
	}

	/**
	 * @param schemaName
	 * @return
	 */
	public Schema getSchema(String schemaName);

	/**
	 * @param clazz
	 * @throws PlainTableException
	 */
	public void manage(Class<? extends SchemaDefinition> clazz)
			throws PlainTableException;

	/**
	 * @param schema
	 * @throws PlainTableException
	 */
	public void manage(Schema schema) throws PlainTableException;

	/**
	 * @param mode
	 * @return
	 * @throws PlainTableException
	 */
	public boolean sync(SynchronizeMode mode) throws PlainTableException;

	/**
	 * @param mode
	 * @param forced
	 * @return
	 * @throws PlainTableException
	 */
	public boolean sync(SynchronizeMode mode, boolean forced)
			throws PlainTableException;

}
