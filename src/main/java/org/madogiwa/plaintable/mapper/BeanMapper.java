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
package org.madogiwa.plaintable.mapper;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.Row;
import org.madogiwa.plaintable.schema.Schema;
import org.madogiwa.plaintable.util.ReflectionUtils;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class BeanMapper<T> implements RowMapper<T> {

	private String sourceAlias;

	private Class<T> beanClass;

	private Map<String, MappingFunction<T>> funcs = new HashMap<String, MappingFunction<T>>();

	/**
	 * @param clazz
	 */
	public BeanMapper(Class<T> clazz) {
		Mapped mapped = clazz.getAnnotation(Mapped.class);
		if (mapped == null) {
			throw new RuntimeException();
		}
		this.beanClass = clazz;
		this.sourceAlias = ReflectionUtils.findSchema(mapped.schema())
				.getAlias().toLowerCase();

		init();
	}

	/**
	 * @param clazz
	 * @param schema
	 */
	public BeanMapper(Class<T> clazz, Schema schema) {
		this(clazz, schema.getAlias());
	}

	/**
	 * @param clazz
	 * @param sourceAlias
	 */
	public BeanMapper(Class<T> clazz, String sourceAlias) {
		this.beanClass = clazz;
		this.sourceAlias = sourceAlias.toLowerCase();

		init();
	}

	/**
	 * 
	 */
	private void init() {
		List<PropertyDescriptor> properties = getProperties(beanClass);
		for (PropertyDescriptor property : properties) {
			final Method method = property.getWriteMethod();
			final String path = (sourceAlias + "." + property.getName())
					.toLowerCase();

			Mapped mapped = property.getPropertyType().getAnnotation(
					Mapped.class);
			if (mapped != null) {
				final BeanMapper mapper = new BeanMapper(
						property.getPropertyType(), property.getName()
								+ "_"
								+ ReflectionUtils.findSchema(mapped.schema())
										.getName());
				funcs.put(path, new MappingFunction<T>() {
					public void map(Row row, T bean) throws Exception {
						Object value = mapper.map(row);
						method.invoke(bean, value);
					}
				});
			} else {
				funcs.put(path, new MappingFunction<T>() {
					public void map(Row row, T bean) throws Exception {
						method.invoke(bean, row.getObject(path));
					}
				});
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.mapper.Mapper#map(org.madogiwa.plaintable.Row)
	 */
	public T map(Row row) throws PlainTableException {
		T bean;

		try {
			bean = beanClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		for (String path : row.getAliasList()) {
			if (funcs.containsKey(path)) {
				try {
					funcs.get(path).map(row, bean);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return bean;
	}

	/**
	 * @param clazz
	 * @return
	 */
	private List<PropertyDescriptor> getProperties(Class<?> clazz) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			return Arrays.asList(beanInfo.getPropertyDescriptors());
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	public interface MappingFunction<T> {

		public void map(Row row, T bean) throws Exception;

	}

}
