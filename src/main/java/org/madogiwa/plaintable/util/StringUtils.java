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
package org.madogiwa.plaintable.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hidenori Sugiyama
 *
 */
public class StringUtils {

	public static String join(List<String> list) {
		return join(list, ",");
	}

	public static String join(List<String> list, String delimiter) {
		StringBuffer str = new StringBuffer();

		if (list.size() > 0) {
			str.append(list.get(0));
			for(int i = 1; i < list.size(); i++) {
				str.append(delimiter);
				str.append(list.get(i));
			}
		}

		return str.toString();
	}

	public static List<String> makeStringList(String word, int count) {
		return makeStringList(word, count, ",");
	}

	public static List<String> makeStringList(String word, int count, String delimiter) {
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < count; i++) {
			list.add(word);
		}
		return list;
	}

}
