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

import java.util.Date;

import org.madogiwa.plaintable.criteria.TableSource;
import org.madogiwa.plaintable.schema.BinaryAttribute;
import org.madogiwa.plaintable.schema.DateTimeAttribute;
import org.madogiwa.plaintable.schema.KeyColumn;
import org.madogiwa.plaintable.schema.NumericAttribute;
import org.madogiwa.plaintable.schema.TextAttribute;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class Values {

	public static BinaryExpression binary(BinaryAttribute column) {
		return new BinaryColumnReference(new TableSource(column.getSchema()),
				column.getName());
	}

	public static DateTimeExpression date(Date date) {
		return new DateTimeRaw(date);
	}

	public static DateTimeExpression date(DateTimeAttribute attr) {
		return new DateTimeColumnReference(attr);
	}

	public static KeyExpression key(long id) {
		return new KeyRaw(id);
	}

	public static KeyExpression key(KeyColumn attr) {
		return new KeyColumnReference(attr);
	}

	public static NumericExpression numeric(Number value) {
		return new NumericRaw(value);
	}

	public static NumericExpression numeric(NumericAttribute attr) {
		return new NumericColumnReference(attr);
	}

	public static StringExpression string(String str) {
		return new StringRaw(str);
	}

	public static StringExpression string(TextAttribute attr) {
		return new StringColumnReference(attr);
	}

}
