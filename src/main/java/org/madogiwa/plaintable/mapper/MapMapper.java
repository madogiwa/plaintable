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
package org.madogiwa.plaintable.mapper;

import java.util.HashMap;
import java.util.Map;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.Row;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class MapMapper implements RowMapper<Map<String, Object>> {

	/**
	 * 
	 */
	public MapMapper() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.mapper.Mapper#map(org.madogiwa.plaintable.Row)
	 */
	public Map<String, Object> map(Row row) throws PlainTableException {
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < row.size(); i++) {
			map.put(row.getAlias(i), row.getObject(i));
		}

		return map;
	}

}
