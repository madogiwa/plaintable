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
package org.madogiwa.plaintable.criteria.bool;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.Resolver;
import org.madogiwa.plaintable.criteria.value.StringExpression;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class LikePredicate implements BooleanExpression, Resolver {

	public enum Type {
		LIKE, NOT_LIKE
	}

	public enum MatchType {
		START_WITH, END_WITH, EXACT, CONTAIN
	}

	private StringExpression value;

	private String pattern;

	private String escapedPattern;

	private Type type;

	private MatchType matchType;

	/**
	 * @param target
	 * @param pattern
	 */
	public LikePredicate(StringExpression value, String pattern, Type type,
			MatchType matchType) {
		this.value = value;
		this.pattern = pattern;
		this.type = type;
		this.matchType = matchType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.simpletable.criteria.Criterion#getSQLString(org.madogiwa
	 * .simpletable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		String marker = context.getResolverMarker(this);
		escapedPattern = context.getDialect().escape(pattern);
		if (type.equals(Type.LIKE)) {
			return String.format("(%s LIKE %s)", value.getSQLString(context),
					marker);
		} else {
			return String.format("(%s NOT LIKE %s)",
					value.getSQLString(context), marker);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.Resolver#resolve(java.sql.PreparedStatement
	 * , int)
	 */
	public void resolve(PreparedStatement statement, int index)
			throws SQLException {

		switch (matchType) {
		case START_WITH:
			statement.setString(index, "%" + escapedPattern);
			break;
		case END_WITH:
			statement.setString(index, escapedPattern + "%");
			break;
		case EXACT:
			statement.setString(index, escapedPattern);
			break;
		case CONTAIN:
			statement.setString(index, "%" + escapedPattern + "%");
			break;
		default:
			throw new RuntimeException();
		}
	}

}
