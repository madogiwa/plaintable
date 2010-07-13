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

import java.util.ArrayList;
import java.util.List;

import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.bool.BooleanExpression;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class CaseExpression implements ValueExpression {

	private ValueExpression defaultValue;

	private List<CaseItem> caseList = new ArrayList<CaseItem>();

	/**
	 * 
	 */
	public CaseExpression() {
		this(new NullRaw());
	}

	/**
	 * @param defaultValue
	 */
	public CaseExpression(ValueExpression defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @param cond
	 * @param value
	 * @return
	 */
	public CaseExpression add(BooleanExpression cond, ValueExpression value) {
		caseList.add(new CaseItem(cond, value));
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.IExpression#getSQLString(org.madogiwa
	 * .plaintable.criteria.Context)
	 */
	public String getSQLString(Context context) {
		StringBuilder sql = new StringBuilder();
		sql.append(" CASE ");
		for (CaseItem item : caseList) {
			sql.append(item.getSQLString(context));
		}
		sql.append(String.format(" ELSE %s ",
				defaultValue.getSQLString(context)));
		sql.append(" END ");
		return sql.toString();
	}

	class CaseItem {
		public BooleanExpression cond;

		public ValueExpression value;

		public CaseItem(BooleanExpression cond, ValueExpression value) {
			this.cond = cond;
			this.value = value;
		}

		public String getSQLString(Context context) {
			return String.format(" WHEN %s THEN %s ",
					cond.getSQLString(context), value.getSQLString(context));
		}
	}

}
