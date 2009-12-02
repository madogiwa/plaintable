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
import java.lang.reflect.InvocationTargetException;
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

	private Map<String, String> mapping = new HashMap<String, String>();

	/**
	 * @param clazz
	 */
	public BeanMapper(Class<T> clazz) {
		Mapped mapping = clazz.getAnnotation(Mapped.class);
		if (mapping != null) {
			this.beanClass = clazz;
			this.sourceAlias = ReflectionUtils.findSchema(mapping.schema()).getAlias();
		} else {
			this.beanClass = clazz;
			this.sourceAlias = null;
		}
	}

	public BeanMapper(Class<T> clazz, Schema schema) {
		this(clazz, schema.getAlias());
	}

	public BeanMapper(Class<T> clazz, String sourceAlias) {
		this.beanClass = clazz;
		this.sourceAlias = sourceAlias;
	}

	/**
	 * @param clazz
	 * @param mapping
	 */
	public BeanMapper(Class<T> clazz, Map<String, String> mapping) {
		beanClass = clazz;
		this.mapping = mapping;
	}

	public BeanMapper<T> add(String path, String property) {
		mapping.put(path, property);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.madogiwa.plaintable.mapper.Mapper#map(org.madogiwa.plaintable.Row)
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

		List<PropertyDescriptor> properties = getProperties(beanClass);
		for(int i = 0; i < row.size(); i++) {
			String alias = row.getAlias(i);
			for(PropertyDescriptor propDesc : properties) {
				String propName;
				if (mapping.containsKey(alias)) {
					propName = mapping.get(alias);
				} else {
					propName = (sourceAlias != null) ? sourceAlias + "." + propDesc.getName() : propDesc.getName();
				}
				if (propName.equalsIgnoreCase(alias)) {
					setProperty(bean, propDesc, row, i);
				}
			}
		}

		return bean;
	}

	/**
	 * @param propDesc
	 * @param row
	 * @param index
	 * @throws PlainTableException 
	 */
	private void setProperty(Object bean, PropertyDescriptor propDesc, Row row, int index) throws PlainTableException {
		try {
			Object value = row.getObject(index);
			Method method = propDesc.getWriteMethod();
			method.invoke(bean, value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
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

	/**
	 * @param name
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	protected Method getSetterMethod(String name, Object arg) {
		try {
			return beanClass.getMethod(getSetterMethodName(name), arg.getClass());
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			// nothing todo
		}
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	private String getSetterMethodName(String name) {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

}
