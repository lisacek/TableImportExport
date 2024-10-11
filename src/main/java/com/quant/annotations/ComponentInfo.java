package com.quant.annotations;

import com.quant.enums.ComponentRender;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentInfo {
    ComponentRender render() default ComponentRender.BASIC;
    String borderLayout() default BorderLayout.CENTER;
}
