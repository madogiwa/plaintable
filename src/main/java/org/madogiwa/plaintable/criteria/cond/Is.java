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
package org.madogiwa.plaintable.criteria.cond;

import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.bool.BooleanExpression;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Is implements ConditionExpression {

	enum Truth {
		TRUE, FALSE, UNKNOWN
	}

	private BooleanExpression expr;

	private Truth truth;

	/**
	 * @param expr
	 * @param truth
	 */
	public Is(BooleanExpression expr, Truth truth) {
		this.expr = expr;
		this.truth = truth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.simpletable.criteria.Criterion#getSQLString(org.madogiwa
	 * .simpletable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		return " ( " + expr.getSQLString(context) + " ) IS " + truth.toString()
				+ " ";
	}

}
