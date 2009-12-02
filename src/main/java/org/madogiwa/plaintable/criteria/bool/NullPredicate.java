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
import org.madogiwa.plaintable.criteria.value.BinaryColumnReference;
import org.madogiwa.plaintable.criteria.value.DateTimeColumnReference;
import org.madogiwa.plaintable.criteria.value.NumericColumnReference;
import org.madogiwa.plaintable.criteria.value.StringColumnReference;
import org.madogiwa.plaintable.criteria.value.ValueExpression;

/**
 * @author Hidenori Sugiyama
 *
 */
public class NullPredicate implements BooleanExpression {

	enum Type {
		IS_NULL,
		IS_NOT_NULL
	}

	private ValueExpression column;

	private Type type;

	/**
	 * @param value
	 */
	public NullPredicate(NumericColumnReference column, Type type) {
		this.column = column;
	}

	/**
	 * @param value
	 */
	public NullPredicate(StringColumnReference column, Type type) {
		this.column = column;
	}

	/**
	 * @param value
	 */
	public NullPredicate(DateTimeColumnReference column, Type type) {
		this.column = column;
	}

	/**
	 * @param value
	 */
	public NullPredicate(BinaryColumnReference column, Type type) {
		this.column = column;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.simpletable.criteria.Criterion#getSQLString(org.madogiwa.simpletable.criteria.CriteriaContext)
	 */
	public String getSQLString(Context context) {
		if (type == Type.IS_NULL) {
			return String.format("(%s IS NULL)", column.getSQLString(context));
		} else {
			return String.format("(%s IS NOT NULL)", column.getSQLString(context));
		}
	}

}
