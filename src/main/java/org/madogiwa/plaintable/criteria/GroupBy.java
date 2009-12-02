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
package org.madogiwa.plaintable.criteria;

import java.util.ArrayList;
import java.util.List;

import org.madogiwa.plaintable.schema.Column;
import org.madogiwa.plaintable.schema.NumericAttribute;

/**
 * @author Hidenori Sugiyama
 *
 */
public class GroupBy {

	private List<Column> groupByList = new ArrayList<Column>();

	public GroupBy() {
		
	}

	public void add(Column column) {
		groupByList.add(column);
	}

	public void groupBy(NumericAttribute column) {
		groupByList.add(column);
	}

	public boolean isGroupBy() {
		return (groupByList.size() == 0) ? false : true;
	}

	public String getSQLString(Context context) {
		StringBuilder sql = new StringBuilder();
		for(Column column : groupByList) {
			sql.append(String.format(" %s, ", column.getName()));
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		return sql.toString();
	}

}
