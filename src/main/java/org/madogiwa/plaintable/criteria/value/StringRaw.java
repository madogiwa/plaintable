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
package org.madogiwa.plaintable.criteria.value;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.Resolver;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class StringRaw implements StringExpression, Resolver {

	private String value;

	/**
	 * @param value
	 */
	public StringRaw(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.Criterion#getSQLString(org.madogiwa.
	 * plaintable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		String marker = context.getResolverMarker(this);
		return String.format("(%s)", marker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.Resolver#resolve(java.sql.PreparedStatement,
	 * int)
	 */
	public void resolve(PreparedStatement statement, int index)
			throws SQLException {
		statement.setString(index, getValue());
	}

	/**
	 * @return
	 */
	protected String getValue() {
		return value;
	}

}
