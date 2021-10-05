package com.icap.i18n;

import com.icap.i18n.exceptions.TranslatableException;
import com.icap.i18n.exceptions.TranslatableIllegalArgumentException;
import com.icap.i18n.exceptions.TranslatableIllegalStateException;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toConcurrentMap;

@SuppressWarnings("PMD.ClassNamingConventions")
public class TranslatableExceptionFactory {

    @SuppressWarnings("PMD.UseConcurrentHashMap")
    private static final Map<String, Class<?>> KEY_TO_EXCEPTION_MAPPING = Stream.of(
            new AbstractMap.SimpleEntry<>(MessageKeys.EMAIL_ADDRESS_ALREADY_EXISTS, TranslatableIllegalStateException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.EMAIL_ADDRESS_MALFORMED, TranslatableIllegalArgumentException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.ORGANIZATION_ALREADY_ENABLED, TranslatableIllegalStateException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.ORGANIZATION_DOES_NOT_EXIST, TranslatableIllegalStateException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.ORGANIZATION_IS_DEREGISTERED, TranslatableIllegalStateException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.ORGANIZATION_IS_DISABLED, TranslatableIllegalStateException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.ORGANIZATION_REGISTRATION_TOKEN_FOR_ANOTHER_ORG, TranslatableIllegalArgumentException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.ORGANIZATION_UPDATE_NO_FIELDS_CHANGED, TranslatableIllegalArgumentException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.USER_ALREADY_ORGANIZATION_ADMIN, TranslatableIllegalStateException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.USER_DISPLAY_NAME_MISSING, TranslatableIllegalArgumentException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.USER_NOT_MEMBER_OF_ORGANIZATION, TranslatableIllegalArgumentException.class),
            new AbstractMap.SimpleEntry<>(MessageKeys.USER_UPDATE_NO_FIELDS_CHANGED, TranslatableIllegalArgumentException.class))
            .collect(toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));

    private TranslatableExceptionFactory() {
    }

    public static void throwForKey(String i18nMessageKey) {
        Class<?> clazz = KEY_TO_EXCEPTION_MAPPING.get(i18nMessageKey);
        throwGenericExceptionIfMessageKeyIsInvalid(i18nMessageKey, clazz);

        try {
            throw (TranslatableException) clazz.getConstructor(String.class).newInstance(i18nMessageKey);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void throwForKey(String i18nMessageKey, Object... args) {
        Class<?> clazz = KEY_TO_EXCEPTION_MAPPING.get(i18nMessageKey);
        throwGenericExceptionIfMessageKeyIsInvalid(i18nMessageKey, clazz);

        try {
            throw (TranslatableException) clazz.getConstructor(String.class, Object[].class)
                    .newInstance(i18nMessageKey, args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void throwForKey(String i18nMessageKey, Throwable cause) {
        Class<?> clazz = KEY_TO_EXCEPTION_MAPPING.get(i18nMessageKey);
        throwGenericExceptionIfMessageKeyIsInvalid(i18nMessageKey, clazz);

        try {
            throw (TranslatableException) clazz.getConstructor(String.class, Throwable.class)
                    .newInstance(i18nMessageKey, cause);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void throwForKey(String i18nMessageKey, Throwable cause, Object... args) {
        Class<?> clazz = KEY_TO_EXCEPTION_MAPPING.get(i18nMessageKey);
        throwGenericExceptionIfMessageKeyIsInvalid(i18nMessageKey, clazz);

        try {
            throw (TranslatableException) clazz.getConstructor(String.class, Throwable.class, Object[].class)
                    .newInstance(i18nMessageKey, cause, args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void throwGenericExceptionIfMessageKeyIsInvalid(String i18nMessageKey, Class<?> clazz) {
        if (clazz == null) {
            throw new RuntimeException(String.format("Invalid message key for translatable exception %s", i18nMessageKey));
        }
    }
}
