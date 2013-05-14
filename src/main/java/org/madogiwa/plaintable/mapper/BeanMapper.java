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

import org.madogiwa.plaintable.PlainTableException;
import org.madogiwa.plaintable.Row;
import org.madogiwa.plaintable.schema.Schema;
import org.madogiwa.plaintable.util.BeanUtils;
import org.madogiwa.plaintable.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class BeanMapper<T> implements RowMapper<T> {

	private String sourceAlias;

	private Class<T> beanClass;

	private boolean recursive = true;

	private boolean initialized = false;

	private Map<String, String> mapping = new HashMap<String, String>();

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
				.getFullName();
	}

	/**
	 * @param clazz
	 * @param schema
	 */
	public BeanMapper(Class<T> clazz, Schema schema) {
		this(clazz, schema.getFullName());
	}

	/**
	 * @param clazz
	 * @param sourceAlias
	 */
	public BeanMapper(Class<T> clazz, String sourceAlias) {
		this.beanClass = clazz;
		this.sourceAlias = sourceAlias;
	}

	/**
	 * @param clazz
	 * @param sourceAlias
	 */
	public BeanMapper(Class<T> clazz, String sourceAlias, boolean recursive) {
		this.beanClass = clazz;
		this.sourceAlias = sourceAlias;
		this.recursive = recursive;
	}

	public BeanMapper add(String property, String path) {
		mapping.put(property, path);
		return this;
	}

	/**
	 * 
	 */
	private void init() {
		List<PropertyDescriptor> properties = getProperties(beanClass);
		for (PropertyDescriptor property : properties) {
			final Method writeMethod = property.getWriteMethod();
			final Method readMethod = property.getReadMethod();
			final String path = (sourceAlias + "." + property.getName())
					.toLowerCase();

			if (readMethod == null || writeMethod == null) {
				continue;
			}

			Type returnType = readMethod.getReturnType();
			Type genericReturnType = readMethod.getGenericReturnType();
			if (List.class.isAssignableFrom((Class)returnType) && genericReturnType instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType)genericReturnType;
				if (parameterizedType.getActualTypeArguments().length != 1) {
					continue;
				}
				Type listType = parameterizedType.getActualTypeArguments()[0];
				Mapped mapped = ((Class<?>)listType).getAnnotation(Mapped.class);
				if (mapped == null) {
					continue;
				}

				if (!recursive) {
					continue;
				}

				String newAlias = property.getName() + "_" + ReflectionUtils.findSchema(mapped.schema()).getName();
				final BeanMapper mapper = new BeanMapper(
					(Class)listType, newAlias, false);

				if (mapping.containsKey(property.getName())) {
					newAlias = mapping.get(property.getName());
				}

				funcs.put("<N>" + newAlias, new MappingFunction<T>() {
					public void map(Row row, T bean) throws Exception {
						List list = (List)readMethod.invoke(bean);
						if (list == null) {
							list = new ArrayList();
							writeMethod.invoke(bean, list);
						}
						list.add(mapper.map(row));
					}
				});
			} else {
				Mapped mapped = property.getPropertyType().getAnnotation(
						Mapped.class);
				if (mapped != null) {
					if (!recursive) {
						continue;
					}

					String newAlias = property.getName() + "_" + ReflectionUtils.findSchema(mapped.schema()).getName();
					final BeanMapper mapper = new BeanMapper(
							property.getPropertyType(), newAlias, false);

					if (mapping.containsKey(property.getName())) {
						newAlias = mapping.get(property.getName());
					}

					funcs.put("<N>" + newAlias, new MappingFunction<T>() {
						public void map(Row row, T bean) throws Exception {
							Object value = mapper.map(row);
							writeMethod.invoke(bean, value);
						}
					});
				} else {
					funcs.put(path, new MappingFunction<T>() {
						public void map(Row row, T bean) throws Exception {
							BeanUtils.setBeanProperty(bean, writeMethod, row.getObject(path));
						}
					});
				}
			}
		}
	}

	public String getSourceAlias() {
		return sourceAlias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.madogiwa.plaintable.mapper.Mapper#map(org.madogiwa.plaintable.Row)
	 */
	public T map(Row row) throws PlainTableException {
		if (!initialized) {
			init();
			initialized = true;
		}

		T bean;

		try {
			bean = beanClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		return map(row, bean);
	}

	public T map(Row row, T bean) throws PlainTableException {
		if (!initialized) {
			init();
			initialized = true;
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

		for (String key : funcs.keySet()) {
			if (key.startsWith("<N>")) {
				String actualKey = key.replaceFirst("<N>", "").toLowerCase();
				for (String path : row.getAliasList()) {
					if (path.startsWith(actualKey + ".")) {
						try {
							funcs.get(key).map(row, bean);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}

						break;
					}
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
