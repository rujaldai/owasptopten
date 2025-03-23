package com.owasptopten.secure.helpers;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Optional;

@UtilityClass
public class ObjectUtil {

    /**
     * Safely returns a default value if the input is null or empty.
     * Handles collections, strings, and other objects.
     *
     * @param value The value to check
     * @param defaultValue The default value to return if the input is null or empty
     * @param <T> The type of the value
     * @return The input value if it's not null or empty, otherwise the default value
     */
    public static <T> T defaultIfEmpty(T value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Collection<?> collection) {
            return collection.isEmpty() ? defaultValue : value;
        }

        if (value instanceof String string) {
            return string.trim().isEmpty() ? defaultValue : value;
        }

        return value;
    }

    /**
     * Safely converts a value to an Optional.
     * Handles null values and empty collections.
     *
     * @param value The value to convert
     * @param <T> The type of the value
     * @return An Optional containing the value if it's not null or empty
     */
    public static <T> Optional<T> toOptional(T value) {
        if (value == null) {
            return Optional.empty();
        }

        if (value instanceof Collection<?> collection) {
            return collection.isEmpty() ? Optional.empty() : Optional.of(value);
        }

        if (value instanceof String string) {
            return string.trim().isEmpty() ? Optional.empty() : Optional.of(value);
        }

        return Optional.of(value);
    }
} 