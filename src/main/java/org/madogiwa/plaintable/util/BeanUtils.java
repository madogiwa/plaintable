package org.madogiwa.plaintable.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 */
public class BeanUtils {

    public static void setBeanProperty(Object bean, Method setter, Object value) {
        try {
            Class parameterClass = setter.getParameterTypes()[0];
            if (parameterClass.isAssignableFrom(boolean.class)) {
                setter.invoke(bean, ((Boolean)value).booleanValue());
            } else if (parameterClass.isAssignableFrom(byte.class)) {
                setter.invoke(bean, ((Byte)value).byteValue());
            } else if (parameterClass.isAssignableFrom(short.class)) {
                setter.invoke(bean, ((Short)value).shortValue());
            } else if (parameterClass.isAssignableFrom(char.class)) {
                setter.invoke(bean, ((Character)value).charValue());
            } else if (parameterClass.isAssignableFrom(int.class)) {
                setter.invoke(bean, ((Integer)value).intValue());
            } else if (parameterClass.isAssignableFrom(long.class)) {
                setter.invoke(bean, ((Long)value).longValue());
            } else if (parameterClass.isAssignableFrom(float.class)) {
                setter.invoke(bean, ((Float)value).floatValue());
            } else if (parameterClass.isAssignableFrom(double.class)) {
                setter.invoke(bean, ((Double)value).doubleValue());
            } else {
                setter.invoke(bean, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
