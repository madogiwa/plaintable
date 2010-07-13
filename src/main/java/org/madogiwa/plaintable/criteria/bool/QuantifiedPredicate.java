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
import org.madogiwa.plaintable.criteria.bool.ComparisonPredicate.Operator;
import org.madogiwa.plaintable.criteria.list.ValueListExpression;
import org.madogiwa.plaintable.criteria.value.ValueExpression;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class QuantifiedPredicate implements BooleanExpression {

	enum Quantified {
		ALL, SOME, ANY
	}

	private ValueExpression value;

	private ValueListExpression list;

	private Quantified quantified;

	private Operator op;

	/**
	 * @param value
	 * @param list
	 * @param op
	 */
	protected QuantifiedPredicate(ValueExpression value,
			ValueListExpression list, Quantified quantified, Operator op) {
		this.value = value;
		this.list = list;
		this.quantified = quantified;
		this.op = op;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.simpletable.criteria.Criterion#getSQLString(org.madogiwa
	 * .simpletable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		return String.format("(%s %s %s (%s)", value.getSQLString(context),
				op.toString(), quantified.toString(),
				list.getSQLString(context));
	}

}
