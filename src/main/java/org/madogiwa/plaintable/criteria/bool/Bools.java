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
package org.madogiwa.plaintable.criteria.bool;

import java.util.Arrays;
import java.util.Date;

import org.madogiwa.plaintable.criteria.Rows;
import org.madogiwa.plaintable.criteria.bool.BetweenPredicate.BetweenType;
import org.madogiwa.plaintable.criteria.bool.ComparisonPredicate.Operator;
import org.madogiwa.plaintable.criteria.bool.NullPredicate.Type;
import org.madogiwa.plaintable.criteria.bool.QuantifiedPredicate.Quantified;
import org.madogiwa.plaintable.criteria.list.DateTimeListExpression;
import org.madogiwa.plaintable.criteria.list.DateTimeListRaw;
import org.madogiwa.plaintable.criteria.list.NumericListExpression;
import org.madogiwa.plaintable.criteria.list.NumericListRaw;
import org.madogiwa.plaintable.criteria.list.StringListExpression;
import org.madogiwa.plaintable.criteria.list.StringListRaw;
import org.madogiwa.plaintable.criteria.list.ValueListExpression;
import org.madogiwa.plaintable.criteria.value.BinaryColumnReference;
import org.madogiwa.plaintable.criteria.value.DateTimeColumnReference;
import org.madogiwa.plaintable.criteria.value.DateTimeExpression;
import org.madogiwa.plaintable.criteria.value.DateTimeRaw;
import org.madogiwa.plaintable.criteria.value.KeyColumnReference;
import org.madogiwa.plaintable.criteria.value.NumericColumnReference;
import org.madogiwa.plaintable.criteria.value.NumericExpression;
import org.madogiwa.plaintable.criteria.value.NumericRaw;
import org.madogiwa.plaintable.criteria.value.StringColumnReference;
import org.madogiwa.plaintable.criteria.value.StringExpression;
import org.madogiwa.plaintable.criteria.value.ValueExpression;
import org.madogiwa.plaintable.schema.DateTimeAttribute;
import org.madogiwa.plaintable.schema.KeyColumn;
import org.madogiwa.plaintable.schema.NumericAttribute;
import org.madogiwa.plaintable.schema.TextAttribute;

/**
 * @author Hidenori Sugiyama
 *
 */
public class Bools {

	public static BetweenPredicate between(NumericColumnReference column, Number low, Number high) {
		return between(column, new NumericRaw(low), new NumericRaw(high));
	}

	public static BetweenPredicate between(NumericExpression target, NumericExpression low, NumericExpression high) {
		return new BetweenPredicate(target, low, high, BetweenType.BETWEEN);
	}

	public static BetweenPredicate between(DateTimeColumnReference column, Date low, Date high) {
		return between(column, new DateTimeRaw(low), new DateTimeRaw(high));
	}

	public static BetweenPredicate between(DateTimeExpression target, DateTimeExpression low, DateTimeExpression high) {
		return new BetweenPredicate(target, low, high, BetweenType.BETWEEN);
	}

	public static BetweenPredicate notBetween(NumericColumnReference column, Number low, Number high) {
		return notBetween(column, new NumericRaw(low), new NumericRaw(high));
	}

	public static BetweenPredicate notBetween(NumericExpression target, NumericExpression low, NumericExpression high) {
		return new BetweenPredicate(target, low, high, BetweenType.NOT_BETWEEN);		
	}

	public static BetweenPredicate notBetween(DateTimeColumnReference column, Date low, Date high) {
		return notBetween(column, new DateTimeRaw(low), new DateTimeRaw(high));
	}

	public static BetweenPredicate notBetween(DateTimeExpression target, DateTimeExpression low, DateTimeExpression high) {
		return new BetweenPredicate(target, low, high, BetweenType.NOT_BETWEEN);		
	}

	public static ComparisonPredicate eq(KeyColumn attr, long key) {
		return eq(new KeyColumnReference(attr), new NumericRaw(key));
	}

	public static ComparisonPredicate eq(NumericAttribute attr, Number value) {
		return eq(new NumericColumnReference(attr), new NumericRaw(value));
	}

	public static ComparisonPredicate eq(ValueExpression lhs, ValueExpression rhs) {
		return equal(lhs, rhs);
	}

	public static ComparisonPredicate equal(ValueExpression lhs, ValueExpression rhs) {
		return new ComparisonPredicate(lhs, rhs, ComparisonPredicate.Operator.EQUAL);
	}

	public static ComparisonPredicate ne(NumericAttribute attr, Number value) {
		return ne(new NumericColumnReference(attr), new NumericRaw(value));
	}

	public static ComparisonPredicate ne(ValueExpression lhs, ValueExpression rhs) {
		return notEqual(lhs, rhs);
	}

	public static ComparisonPredicate notEqual(ValueExpression lhs, ValueExpression rhs) {
		return new ComparisonPredicate(lhs, rhs, ComparisonPredicate.Operator.EQUAL);
	}

	public static ComparisonPredicate gt(NumericAttribute attr, Number value) {
		return gt(new NumericColumnReference(attr), new NumericRaw(value));
	}

	public static ComparisonPredicate gt(ValueExpression lhs, ValueExpression rhs) {
		return greaterThan(lhs, rhs);
	}

	public static ComparisonPredicate greaterThan(ValueExpression lhs, ValueExpression rhs) {
		return new ComparisonPredicate(lhs, rhs, ComparisonPredicate.Operator.EQUAL);
	}

	public static ComparisonPredicate ge(NumericAttribute attr, Number value) {
		return ge(new NumericColumnReference(attr), new NumericRaw(value));
	}

	public static ComparisonPredicate ge(ValueExpression lhs, ValueExpression rhs) {
		return greaterThanOrEqual(lhs, rhs);
	}

	public static ComparisonPredicate greaterThanOrEqual(ValueExpression lhs, ValueExpression rhs) {
		return new ComparisonPredicate(lhs, rhs, ComparisonPredicate.Operator.EQUAL);
	}

	public static ComparisonPredicate lt(NumericAttribute attr, Number value) {
		return lt(new NumericColumnReference(attr), new NumericRaw(value));
	}

	public static ComparisonPredicate lt(NumericColumnReference column, Number value) {
		return lt(column, value);
	}

	public static ComparisonPredicate lt(ValueExpression lhs, ValueExpression rhs) {
		return littleThan(lhs, rhs);
	}

	public static ComparisonPredicate littleThan(ValueExpression lhs, ValueExpression rhs) {
		return new ComparisonPredicate(lhs, rhs, ComparisonPredicate.Operator.EQUAL);
	}

	public static ComparisonPredicate le(NumericAttribute attr, Number value) {
		return le(new NumericColumnReference(attr), new NumericRaw(value));
	}

	public static ComparisonPredicate le(ValueExpression lhs, ValueExpression rhs) {
		return littleThanOrEqual(lhs, rhs);
	}

	public static ComparisonPredicate littleThanOrEqual(ValueExpression lhs, ValueExpression rhs) {
		return new ComparisonPredicate(lhs, rhs, ComparisonPredicate.Operator.EQUAL);
	}

	public static ExistsPredicate exists(Rows rows) {
		return new ExistsPredicate(rows);
	}

	public static InPredicate in(KeyColumn column, Long[] list) {
		return in(new KeyColumnReference(column), new NumericListRaw(Arrays.asList(list)));
	}

	public static InPredicate in(KeyColumnReference column, NumericListExpression list) {
		return new InPredicate(column, list);
	}

	public static <N extends Number> InPredicate in(NumericAttribute attr, N[] list) {
		return in(new NumericColumnReference(attr), new NumericListRaw(Arrays.asList(list)));
	}

	public static InPredicate in(NumericColumnReference column, NumericListExpression list) {
		return new InPredicate(column, list);
	}

	public static InPredicate in(TextAttribute attr, String[] values) {
		return in(new StringColumnReference(attr), new StringListRaw(Arrays.asList(values)));
	}

	public static InPredicate in(StringExpression value, StringListExpression list) {
		return new InPredicate(value, list);
	}

	public static InPredicate in(DateTimeAttribute attr, Date[] values) {
		return in(new DateTimeColumnReference(attr), new DateTimeListRaw(Arrays.asList(values)));
	}

	public static InPredicate in(DateTimeExpression value, DateTimeListExpression list) {
		return new InPredicate(value, list);
	}

	public static LikePredicate exact(TextAttribute attr, String pattern) {
		return exact(new StringColumnReference(attr), pattern);
	}

	public static LikePredicate exact(StringColumnReference column, String pattern) {
		return new LikePredicate(column, pattern, LikePredicate.Type.LIKE, LikePredicate.MatchType.EXACT);
	}

	public static LikePredicate contain(TextAttribute attr, String pattern) {
		return contain(new StringColumnReference(attr), pattern);
	}

	public static LikePredicate contain(StringColumnReference column, String pattern) {
		return new LikePredicate(column, pattern, LikePredicate.Type.LIKE, LikePredicate.MatchType.CONTAIN);
	}

	public static LikePredicate startWith(TextAttribute column, String pattern) {
		return startWith(new StringColumnReference(column), pattern);
	}

	public static LikePredicate startWith(StringColumnReference column, String pattern) {
		return new LikePredicate(column, pattern, LikePredicate.Type.LIKE, LikePredicate.MatchType.START_WITH);
	}

	public static LikePredicate endWith(TextAttribute column, String pattern) {
		return endWith(new StringColumnReference(column), pattern);
	}

	public static LikePredicate endWith(StringColumnReference column, String pattern) {
		return new LikePredicate(column, pattern, LikePredicate.Type.LIKE, LikePredicate.MatchType.END_WITH);
	}

	public static LikePredicate notExact(TextAttribute attr, String pattern) {
		return notExact(new StringColumnReference(attr), pattern);
	}

	public static LikePredicate notExact(StringColumnReference column, String pattern) {
		return new LikePredicate(column, pattern, LikePredicate.Type.NOT_LIKE, LikePredicate.MatchType.EXACT);
	}

	public static LikePredicate notContain(TextAttribute attr, String pattern) {
		return notContain(new StringColumnReference(attr), pattern);
	}

	public static LikePredicate notContain(StringColumnReference column, String pattern) {
		return new LikePredicate(column, pattern, LikePredicate.Type.NOT_LIKE, LikePredicate.MatchType.CONTAIN);
	}

	public static LikePredicate notStartWith(TextAttribute attr, String pattern) {
		return notStartWith(new StringColumnReference(attr), pattern);
	}

	public static LikePredicate notStartWith(StringColumnReference column, String pattern) {
		return new LikePredicate(column, pattern, LikePredicate.Type.NOT_LIKE, LikePredicate.MatchType.START_WITH);
	}

	public static LikePredicate notEndWith(TextAttribute column, String pattern) {
		return notEndWith(new StringColumnReference(column), pattern);
	}

	public static LikePredicate notEndWith(StringColumnReference column, String pattern) {
		return new LikePredicate(column, pattern, LikePredicate.Type.NOT_LIKE, LikePredicate.MatchType.END_WITH);
	}

	public static NullPredicate isNull(NumericColumnReference column) {
		return new NullPredicate(column, Type.IS_NULL);
	}

	public static NullPredicate isNull(StringColumnReference column) {
		return new NullPredicate(column, Type.IS_NULL);
	}

	public static NullPredicate isNull(DateTimeColumnReference column) {
		return new NullPredicate(column, Type.IS_NULL);
	}

	public static NullPredicate isNull(BinaryColumnReference column) {
		return new NullPredicate(column, Type.IS_NULL);
	}

	public static NullPredicate isNotNull(NumericColumnReference column) {
		return new NullPredicate(column, Type.IS_NOT_NULL);
	}

	public static NullPredicate isNotNull(StringColumnReference column) {
		return new NullPredicate(column, Type.IS_NOT_NULL);
	}

	public static NullPredicate isNotNull(DateTimeColumnReference column) {
		return new NullPredicate(column, Type.IS_NOT_NULL);
	}

	public static NullPredicate isNotNull(BinaryColumnReference column) {
		return new NullPredicate(column, Type.IS_NOT_NULL);
	}

	public static QuantifiedPredicate equalAll(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ALL, Operator.EQUAL);
	}

	public static QuantifiedPredicate equalSome(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.SOME, Operator.EQUAL);
	}

	public static QuantifiedPredicate equalAny(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ANY, Operator.EQUAL);
	}

	public static QuantifiedPredicate notEqualAll(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ALL, Operator.NOT_EQUAL);
	}

	public static QuantifiedPredicate notEqualSome(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.SOME, Operator.NOT_EQUAL);
	}

	public static QuantifiedPredicate notEqualAny(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ANY, Operator.NOT_EQUAL);
	}

	public static QuantifiedPredicate littleThanAll(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ALL, Operator.LITTLE_THAN);
	}

	public static QuantifiedPredicate littleThanSome(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.SOME, Operator.LITTLE_THAN);
	}

	public static QuantifiedPredicate littleThanAny(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ANY, Operator.LITTLE_THAN);
	}

	public static QuantifiedPredicate littleThanOrEqualAll(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ALL, Operator.LITTLE_THAN_OR_EQUAL);
	}

	public static QuantifiedPredicate littleThanOrEqualSome(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.SOME, Operator.LITTLE_THAN_OR_EQUAL);
	}

	public static QuantifiedPredicate littleThanOrEqualAny(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ANY, Operator.LITTLE_THAN_OR_EQUAL);
	}

	public static QuantifiedPredicate greaterThanAll(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ALL, Operator.GREATER_THAN);
	}

	public static QuantifiedPredicate greaterThanSome(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.SOME, Operator.GREATER_THAN);
	}

	public static QuantifiedPredicate greaterThanAny(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ANY, Operator.GREATER_THAN);
	}

	public static QuantifiedPredicate greaterThanOrEqualAll(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ALL, Operator.GREATER_THAN_OR_EQUAL);
	}

	public static QuantifiedPredicate greaterThanOrEqualSome(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.SOME, Operator.GREATER_THAN_OR_EQUAL);
	}

	public static QuantifiedPredicate greaterThanOrEqualAny(ValueExpression value, ValueListExpression list) {
		return new QuantifiedPredicate(value, list, Quantified.ANY, Operator.GREATER_THAN_OR_EQUAL);
	}

}
