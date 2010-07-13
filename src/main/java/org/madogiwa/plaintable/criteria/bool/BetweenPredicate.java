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

import org.madogiwa.plaintable.criteria.Context;
import org.madogiwa.plaintable.criteria.value.DateTimeExpression;
import org.madogiwa.plaintable.criteria.value.NumericExpression;
import org.madogiwa.plaintable.criteria.value.ValueExpression;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class BetweenPredicate implements BooleanExpression {

	enum BetweenType {
		BETWEEN, NOT_BETWEEN
	}

	private ValueExpression target;

	private ValueExpression low;

	private ValueExpression high;

	private BetweenType type;

	/**
	 * @param target
	 * @param low
	 * @param high
	 * @param type
	 */
	public BetweenPredicate(NumericExpression target, NumericExpression low,
			NumericExpression high, BetweenType type) {
		this.target = target;
		this.low = low;
		this.high = high;
		this.type = type;
	}

	/**
	 * @param target
	 * @param low
	 * @param high
	 * @param type
	 */
	public BetweenPredicate(DateTimeExpression target, DateTimeExpression low,
			DateTimeExpression high, BetweenType type) {
		this.target = target;
		this.low = low;
		this.high = high;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.simpletable.criteria.Criterion#getSQLString(org.madogiwa
	 * .simpletable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		if (type == BetweenType.BETWEEN) {
			return String.format("(%s BETWEEN %s AND %s)",
					target.getSQLString(context), low.getSQLString(context),
					high.getSQLString(context));
		} else {
			return String.format("(%s NOT BETWEEN %s AND %s)",
					target.getSQLString(context), low.getSQLString(context),
					high.getSQLString(context));
		}
	}

}
