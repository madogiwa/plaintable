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
import org.madogiwa.plaintable.criteria.value.ValueExpression;

/**
 * @author Hidenori Sugiyama
 *
 */
public class ComparisonPredicate implements BooleanExpression {

	public enum Operator {
		EQUAL("="),
		NOT_EQUAL("<>"),
		GREATER_THAN(">"),
		GREATER_THAN_OR_EQUAL(">="),
		LITTLE_THAN("<"),
		LITTLE_THAN_OR_EQUAL("<=");

		private String op;

		private Operator(String op) {
			this.op = op;
		}

		public String toString() {
			return op;
		}
	}

	private ValueExpression lhs;

	private ValueExpression rhs;

	private Operator op;

	/**
	 * @param lhs
	 * @param rhs
	 * @param op
	 */
	public ComparisonPredicate(ValueExpression lhs, ValueExpression rhs, Operator op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.simpletable.criteria.Criterion#getSQLString(org.madogiwa.simpletable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		return String.format("(%s %s %s)", lhs.getSQLString(context), op.toString(), rhs.getSQLString(context));
	}

}
