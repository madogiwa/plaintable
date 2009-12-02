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

/**
 * @author Hidenori Sugiyama
 *
 */
public class JoinedSource implements ISource {

	public enum JoinType {
		INNER("INNER JOIN"),
		LEFT("LEFT JOIN"),
		RIGHT("RIGHT JOIN");
	
		private String sql;

		private JoinType(String sql) {
			this.sql = sql;
		}

		public String toString() {
			return sql;
		}
	}

	private ISource left;

	private String leftColumn;

	private ISource right;

	private String rightColumn;

	private JoinType type;

	private String alias;

	/**
	 * @param left
	 * @param leftColumn
	 * @param right
	 * @param rightColumn
	 * @param type
	 */
	public JoinedSource(ISource left, String leftColumn, ISource right, String rightColumn, JoinType type) {
		this(left, leftColumn, right, rightColumn, type, left.getAlias() + "_" + right.getAlias());
	}

	/**
	 * @param left
	 * @param leftColumn
	 * @param right
	 * @param rightColumn
	 * @param type
	 * @param alias
	 */
	public JoinedSource(ISource left, String leftColumn, ISource right, String rightColumn, JoinType type, String alias) {
		this.left = left;
		this.leftColumn = leftColumn;
		this.right = right;
		this.rightColumn = rightColumn;
		this.alias = alias;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.criteria.ISource#getAlias()
	 */
	public String getAlias() {
		return alias;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.criteria.IExpression#getSQLString(org.madogiwa.plaintable.criteria.Context)
	 */
	public String getSQLString(Context context) {
		return String.format("%s %s %s ON %s.%s = %s.%s", left, type, right, left, leftColumn, right, rightColumn);
	}

}
