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
package org.madogiwa.plaintable.criteria.cond;

import org.madogiwa.plaintable.criteria.bool.BooleanExpression;
import org.madogiwa.plaintable.criteria.cond.Is.Truth;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Conds {

	public static And and(BooleanExpression lhs, BooleanExpression rhs) {
		return new And(lhs, rhs);
	}

	public static Is is(BooleanExpression expr, Truth truth) {
		return new Is(expr, truth);
	}

	public static Is isTrue(BooleanExpression expr) {
		return new Is(expr, Truth.TRUE);
	}

	public static Is isFalse(BooleanExpression expr) {
		return new Is(expr, Truth.FALSE);
	}

	public static Is isUnknown(BooleanExpression expr) {
		return new Is(expr, Truth.UNKNOWN);
	}

	public static IsNot isNot(BooleanExpression expr, Truth truth) {
		return new IsNot(expr, truth);
	}

	public static IsNot isNotTrue(BooleanExpression expr) {
		return new IsNot(expr, Truth.TRUE);
	}

	public static IsNot isNotFalse(BooleanExpression expr) {
		return new IsNot(expr, Truth.FALSE);
	}

	public static IsNot isNotUnknown(BooleanExpression expr) {
		return new IsNot(expr, Truth.UNKNOWN);
	}

	public static Not not(BooleanExpression expr) {
		return new Not(expr);
	}

	public static Or or(BooleanExpression lhs, BooleanExpression rhs) {
		return new Or(lhs, rhs);
	}
}
