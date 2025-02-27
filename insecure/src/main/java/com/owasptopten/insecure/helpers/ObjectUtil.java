package com.owasptopten.insecure.helpers;

import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class ObjectUtil {

    public static <T>  T defaultIfEmpty(T value, T defaultValue) {
        if (value == null
        || (value instanceof Collection<?> list && list.isEmpty())) {
            return defaultValue;
        }
        return value;
    }
}
