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
package org.madogiwa.plaintable.dialect;

import org.madogiwa.plaintable.criteria.Window;
import org.madogiwa.plaintable.schema.ReferenceKey;
import org.madogiwa.plaintable.schema.SyntheticKey;
import org.madogiwa.plaintable.schema.attr.BignumAttribute;
import org.madogiwa.plaintable.schema.attr.BooleanAttribute;
import org.madogiwa.plaintable.schema.attr.BytesAttribute;
import org.madogiwa.plaintable.schema.attr.CharAttribute;
import org.madogiwa.plaintable.schema.attr.DateAttribute;
import org.madogiwa.plaintable.schema.attr.DoubleAttribute;
import org.madogiwa.plaintable.schema.attr.FloatAttribute;
import org.madogiwa.plaintable.schema.attr.IntegerAttribute;
import org.madogiwa.plaintable.schema.attr.LongAttribute;
import org.madogiwa.plaintable.schema.attr.ShortAttribute;
import org.madogiwa.plaintable.schema.attr.StreamAttribute;
import org.madogiwa.plaintable.schema.attr.StringAttribute;
import org.madogiwa.plaintable.schema.attr.TimeAttribute;
import org.madogiwa.plaintable.schema.attr.TimestampAttribute;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class PostgreSQLDialect extends GenericDialect {

	private Map<Type, String> typeMap = new HashMap<Type, String>();

	public PostgreSQLDialect() {
		typeMap.put(BooleanAttribute.class, "BOOLEAN");
		typeMap.put(ShortAttribute.class, "INT2");
		typeMap.put(IntegerAttribute.class, "INT4");
		typeMap.put(LongAttribute.class, "INT8");
		typeMap.put(BignumAttribute.class, "NUMERIC");
		typeMap.put(FloatAttribute.class, "FLOAT4");
		typeMap.put(DoubleAttribute.class, "FLOAT8");
		typeMap.put(TimeAttribute.class, "TIME");
		typeMap.put(DateAttribute.class, "DATE");
		typeMap.put(TimestampAttribute.class, "TIMESTAMP");
		typeMap.put(CharAttribute.class, "VARCHAR");
		typeMap.put(StringAttribute.class, "TEXT");
		typeMap.put(BytesAttribute.class, "BYTEA");
		typeMap.put(StreamAttribute.class, "BYTEA");
		typeMap.put(ReferenceKey.class, "INT8");
		typeMap.put(SyntheticKey.class, "BIGSERIAL");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.dialect.Dialect#getSQLType(java.lang.reflect.
	 * Type, int)
	 */
	public String getSQLType(Type type, int length) {
		String sqlType = typeMap.get(type);
		if (sqlType == null) {
			throw new RuntimeException(String.format("undefiend type %s",
					type.toString()));
		}

		if (type.equals(CharAttribute.class)) {
			int charLen = (length == -1) ? MAX_CHAR_LENGTH : length;
			if (charLen <= 0 || charLen > MAX_CHAR_LENGTH) {
				throw new RuntimeException(String.format(
						"invalid length %d type %s", length, type));
			}
			sqlType = String.format("%s(%d)", sqlType, charLen);
		} else if (type.equals(BytesAttribute.class)) {
			int charLen = (length == -1) ? MAX_BYTES_LENGTH : length;
			if (charLen <= 0 || charLen > MAX_BYTES_LENGTH) {
				throw new RuntimeException(String.format(
						"invalid length %d type %s", length, type));
			}
		}

		return sqlType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.dialect.Dialect#isLimitSupported()
	 */
	public boolean isLimitSupported() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.dialect.Dialect#getLimitFragment(long, long)
	 */
	public String getLimitFragment(long offset, long count) {
        if (offset == 0 && count == Window.UNLIMITED) {
            return "";
        }

        if (count == Window.UNLIMITED) {
            return String.format("OFFSET %d", offset);
        } else {
            return String.format("OFFSET %d LIMIT %d", offset, count);
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.dialect.Dialect#quote(java.lang.String)
	 */
	public String quote(String identifier) {
		return "\"" + identifier + "\"";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.dialect.Dialect#escape(java.lang.String)
	 */
	public String escape(String literal) {
		return literal.replace("%", "\\%").replace("_", "\\_");
	}

}
