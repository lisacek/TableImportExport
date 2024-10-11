package com.quant.columns;

import com.quant.annotations.ColumnName;
import com.quant.utils.ReflectionUtils;
import com.quant.validators.Validator;

public interface Column<V extends Validator, T> {

    public String getName();

    public V getValidator();


    default boolean isEditable() {
        return true;
    }

    default String getDisplayFormat() {
        return null;
    }

    default String formatDisplayValue(Object value) {
        if (getDisplayFormat() == null) {
            return value.toString();
        }

        return String.format(getDisplayFormat(), value);
    }

    default String getEditFormat() {
        return null;
    }

    public default Class<T> getValueType() {
        return (Class<T>) ReflectionUtils.getGenericType(this.getClass(), 1);
    }

    public default Object resolveValue(Object obj) {
        var clazz = obj.getClass();
        for (var field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ColumnName.class)) {
                var annotation = field.getAnnotation(ColumnName.class);
                if (annotation.value().equals(getName())) {
                    try {
                        field.setAccessible(true);
                        return field.get(obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (field.getName().equalsIgnoreCase(getName())) {
                try {
                    field.setAccessible(true);
                    return field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public default void assignValue(Object obj, Object value){
        var clazz = obj.getClass();
        for (var field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ColumnName.class)) {
                var annotation = field.getAnnotation(ColumnName.class);
                if (annotation.value().equalsIgnoreCase(getName())) {
                    field.setAccessible(true);
                    try {
                        field.set(obj, ReflectionUtils.normalizeValue(value, field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                continue;
            }

            try {
                if (field.getName().equalsIgnoreCase(getName())) {
                    field.setAccessible(true);
                    try {
                        field.set(obj, ReflectionUtils.normalizeValue(value, field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
