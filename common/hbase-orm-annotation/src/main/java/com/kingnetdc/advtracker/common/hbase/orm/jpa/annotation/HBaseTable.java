package com.kingnetdc.advtracker.common.hbase.orm.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * hbase column family annotation
 * <p>
 * Default Name is ""
 * <p>
 * Created by william on 2017/5/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HBaseTable {
    String name() default "";

    String defaultFamily() default "";
}
