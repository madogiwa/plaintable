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
package org.madogiwa.plaintable.provider;

import java.util.HashMap;
import java.util.Map;

import org.madogiwa.plaintable.criteria.value.ValueExpression;
import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.Schema;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class SimpleRowProvider implements RowProvider {

	private Schema schema;

	private Map<Column, ValueExpression> valueMap;

	public SimpleRowProvider(Schema schema) {
		this(schema, new HashMap<Column, ValueExpression>());
	}

	public SimpleRowProvider(Schema schema, Map<Column, ValueExpression> map) {
		this.schema = schema;
		valueMap = map;
	}

	public RowProvider add(Column column, ValueExpression value) {
		valueMap.put(column, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.provider.RowProvider#getSchema()
	 */
	public Schema getSchema() {
		return schema;
	}

	public Map<Column, ValueExpression> getMap() {
		return valueMap;
	}

}
