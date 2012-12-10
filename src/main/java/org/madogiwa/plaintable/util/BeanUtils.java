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
            setter.invoke(bean, parameterClass.cast(value));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
