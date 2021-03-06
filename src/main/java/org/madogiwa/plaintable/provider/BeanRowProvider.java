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
package org.madogiwa.plaintable.provider;

import org.madogiwa.plaintable.criteria.value.*;
import org.madogiwa.plaintable.mapper.Mapped;
import org.madogiwa.plaintable.schema.*;
import org.madogiwa.plaintable.schema.attr.*;
import org.madogiwa.plaintable.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Hidenori Sugiyama
 * 
 */
public class BeanRowProvider<T> implements RowProvider {

	private Schema schema;

	private T bean;

	private Map<Column, String> mapping;

	private static final Logger logger = Logger.getLogger(BeanRowProvider.class.getName());

	/**
	 * @param bean
	 */
	public BeanRowProvider(T bean) {
		Provided provided = bean.getClass().getAnnotation(Provided.class);
		if (provided == null) {
			throw new RuntimeException();
		}

		this.schema = ReflectionUtils.findSchema(provided.schema());
		logger.finest(bean + " : " + provided.schema() + " : " + schema.getName());

		this.bean = bean;
		this.mapping = new HashMap<Column, String>();
	}

	public BeanRowProvider(T bean, Map<Column, String> columnMapping) {
		Mapped mapping = bean.getClass().getAnnotation(Mapped.class);
		if (mapping == null) {
			throw new RuntimeException();
		}

		this.schema = ReflectionUtils.findSchema(mapping.schema());
		this.bean = bean;
		this.mapping = columnMapping;
	}

	/**
	 * @param bean
	 */
	public BeanRowProvider(Schema schema, T bean) {
		this(schema, bean, new HashMap<Column, String>());
	}

	/**
	 * @param schema
	 * @param bean
	 * @param map
	 */
	public BeanRowProvider(Schema schema, T bean, Map<Column, String> mapping) {
		this.schema = schema;
		this.bean = bean;
		this.mapping = mapping;
	}

	/**
	 * @param column
	 * @param property
	 * @return
	 */
	public BeanRowProvider<T> add(Column column, String property) {
		mapping.put(column, property);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.provider.RowProvider#getSchema()
	 */
	public Schema getSchema() {
		return schema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.madogiwa.plaintable.provider.RowProvider#getMap()
	 */
	public Map<Column, ValueExpression> getMap() {
		Map<Column, ValueExpression> map = new HashMap<Column, ValueExpression>();

		for (Column column : schema.getColumns()) {
			if (column.getClass().equals(SyntheticKey.class)) {
				continue;
			}

			String property = mapping.get(column);
			if (property == null) {
				property = column.getName();
			}
			Object value = getPropertyValue(bean, property);

			if (column.getClass().equals(ReferenceKey.class) && !value.getClass().equals(Long.class)) {
				value = getPrimaryKey(value);
			}
			ValueExpression expr = objectToValueExpression(column, value);
			map.put(column, expr);
		}

		return map;
	}

	/**
	 * @param column
	 * @param value
	 * @return
	 */
	private ValueExpression objectToValueExpression(Column column, Object value) {
		if (value == null) {
			return new NullRaw();
		}

		Class<?> type = column.getClass();
		if (type.equals(ReferenceKey.class)) {
			return new KeyRaw((Long) value);
		} else if (type.equals(IntegerAttribute.class)
				|| type.equals(LongAttribute.class)) {
			return new NumericRaw((Number) value);
		} else if (type.equals(StringAttribute.class)
				|| type.equals(CharAttribute.class)) {
			return new StringRaw((String) value);
		} else if (type.equals(SchemaReference.class)) {
			return new KeyRaw((Long) value);
		} else if (type.equals(TimestampAttribute.class)) {
			return new DateTimeRaw((Date) value);
		} else {
			throw new RuntimeException("unsupported column type");
		}
	}

	/**
	 * @param target
	 * @param propertyName
	 * @return
	 */
	protected Object getPropertyValue(Object target, String propertyName) {
		try {
			Method method = getGetterMethod(propertyName);
			if (method != null) {
				return method.invoke(bean);
			} else {
				return null;
			}
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param name
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	protected Method getGetterMethod(String name) {
		try {
			return bean.getClass().getMethod(getGetterMethodName(name));
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
	private String getGetterMethodName(String name) {
		return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private Long getPrimaryKey(Object object) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor[] descs = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor desc : descs) {
				if ("id".equals(desc.getName())) {
					return (Long)desc.getReadMethod().invoke(object);
				}
			}
			return null;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

}
