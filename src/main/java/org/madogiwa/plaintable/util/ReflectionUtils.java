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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.madogiwa.plaintable.schema.Schema;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class ReflectionUtils {

	public static Schema findSchema(Class<?> clazz) {
		for (Field field : clazz.getFields()) {
			if (Modifier.isStatic(field.getModifiers())
					&& Modifier.isPublic(field.getModifiers())
					&& field.getType().equals(Schema.class)) {
				try {
					return (Schema) field.get(null);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return null;
	}

}
