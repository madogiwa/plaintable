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
import org.madogiwa.plaintable.criteria.list.DateTimeListExpression;
import org.madogiwa.plaintable.criteria.list.NumericListExpression;
import org.madogiwa.plaintable.criteria.list.StringListExpression;
import org.madogiwa.plaintable.criteria.list.ValueListExpression;
import org.madogiwa.plaintable.criteria.value.DateTimeExpression;
import org.madogiwa.plaintable.criteria.value.KeyExpression;
import org.madogiwa.plaintable.criteria.value.NumericExpression;
import org.madogiwa.plaintable.criteria.value.StringExpression;
import org.madogiwa.plaintable.criteria.value.ValueExpression;

/**
 * @author Hidenori Sugiyama
 *
 */
public class InPredicate implements BooleanExpression {

	private ValueExpression value;

	private ValueListExpression list;

	/**
	 * @param value
	 * @param list
	 */
	public InPredicate(KeyExpression value, NumericListExpression list) {
		this.value = value;
		this.list = list;
	}

	/**
	 * @param value
	 * @param list
	 */
	public InPredicate(NumericExpression value, NumericListExpression list) {
		this.value = value;
		this.list = list;
	}

	/**
	 * @param value
	 * @param list
	 */
	public InPredicate(StringExpression value, StringListExpression list) {
		this.value = value;
		this.list = list;
	}

	/**
	 * @param value
	 * @param list
	 */
	public InPredicate(DateTimeExpression value, DateTimeListExpression list) {
		this.value = value;
		this.list = list;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.simpletable.criteria.Criterion#getSQLString(org.madogiwa.simpletable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		return String.format("%s IN (%s)", value.getSQLString(context), list.getSQLString(context));
	}

}
