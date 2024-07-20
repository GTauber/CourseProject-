package com.ead.authuser.utils;

import static org.springframework.util.StringUtils.capitalize;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.springframework.util.ReflectionUtils;

public class MethodUtils {

    private MethodUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String toSnakeCase(String camelCaseString) {
        return camelCaseString.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public static Method getAccessorMethod(Field field) {
        var accessorPrefix = field.getType() == boolean.class ? "is" : "get";
        return ReflectionUtils.findMethod(field.getDeclaringClass(), accessorPrefix + capitalize(field.getName()));
    }

    public static Method getSetterMethod(Field field) {
        var setterPrefix = "set";
        return ReflectionUtils.findMethod(field.getDeclaringClass(), setterPrefix + capitalize(field.getName()), field.getType());
    }

}
