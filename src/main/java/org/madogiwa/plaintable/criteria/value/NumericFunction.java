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

import org.madogiwa.plaintable.criteria.Context;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class NumericFunction implements NumericExpression {

	public enum Function {
		ABS, SIGN
	}

	private NumericExpression expr;

	private Function func;

	/**
	 * @param expr
	 */
	public NumericFunction(NumericExpression expr, Function func) {
		this.expr = expr;
		this.func = func;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.criteria.Expression#getSQLString(org.madogiwa
	 * .plaintable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		return String.format("%s(%s)", func.toString(), expr.getSQLString(context));
	}

}
