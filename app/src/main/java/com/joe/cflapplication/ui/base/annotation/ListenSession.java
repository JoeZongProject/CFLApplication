package com.joe.cflapplication.ui.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Autor zongdongdong on 2016/9/21.
 * 监听session过期(定制)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenSession {
    boolean session() default true;
}

