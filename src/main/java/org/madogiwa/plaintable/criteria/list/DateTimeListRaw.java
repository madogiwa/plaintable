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
package org.madogiwa.plaintable.criteria.list;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.Resolver;

/**
 * @author Hidenori Sugiyama
 *
 */
public class DateTimeListRaw implements DateTimeListExpression {

	private List<Date> list;

	/**
	 * @param list
	 */
	public DateTimeListRaw(List<Date> list) {
		list = new ArrayList<Date>(list);
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.criteria.Criterion#getSQLString(org.madogiwa.plaintable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		if (list.size() == 0) {
			return "";
		}

		StringBuilder sql = new StringBuilder();

		for(final Date date : list) {
			String marker = context.getResolverMarker(new Resolver() {

				public void resolve(PreparedStatement statement, int index)
						throws SQLException {

					statement.setDate(index, new java.sql.Date(date.getTime()));
				}
				
			});
			sql.append(String.format("%s,", marker));
		}
		sql.deleteCharAt(sql.lastIndexOf(","));

		return sql.toString();
	}

}
