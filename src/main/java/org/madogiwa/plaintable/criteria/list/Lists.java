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
package org.madogiwa.plaintable.criteria.list;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.madogiwa.plaintable.criteria.Query;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Lists {

	public static KeyListExpression keyList(List<Long> list) {
		return new KeyListRaw(list);
	}

	public static KeyListExpression keyList(Query criteria) {
		return new KeyListQuery(criteria);
	}

	public static DateTimeListExpression list(Date[] array) {
		return dateTimeList(Arrays.asList(array));
	}

	public static DateTimeListExpression dateTimeList(List<Date> list) {
		return new DateTimeListRaw(list);
	}

	public static DateTimeListExpression dateTimeList(Query criteria) {
		return new DateTimeListQuery(criteria);
	}

	public static NumericListExpression list(Number[] array) {
		return numericList(Arrays.asList(array));
	}

	public static NumericListExpression numericList(List<Number> list) {
		return new NumericListRaw(list);
	}

	public static NumericListExpression numericList(Query criteria) {
		return new NumericListQuery(criteria);
	}

	public static StringListExpression list(String[] array) {
		return stringList(Arrays.asList(array));
	}

	public static StringListExpression stringList(List<String> list) {
		return new StringListRaw(list);
	}

	public static StringListExpression stringList(Query criteria) {
		return new StringListQuery(criteria);
	}

}
