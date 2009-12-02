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

import java.lang.reflect.Type;

/**
 * @author Hidenori Sugiyama
 *
 */
public interface Dialect {

	public static int MAX_CHAR_LENGTH = 254;

	public static int MAX_BYTES_LENGTH = 30000;

	public String getSQLType(Type type, int length);

	public boolean isLimitSupported();

	public String getLimitFragment(long offset, long count);

	public String quote(String identifier);

	public String escape(String literal);

}
