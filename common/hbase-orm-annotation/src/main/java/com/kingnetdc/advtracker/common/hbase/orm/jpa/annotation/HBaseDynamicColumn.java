package com.kingnetdc.advtracker.common.hbase.orm.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * hbase dynamic column define
 *
 * Delimiter can automatic split column name
 * 
 * default Delimiter is "|" 
 * 
 * 
 *Created by william on 2017/5/23.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface HBaseDynamicColumn {
	String prefixName() default "";
	String delimiter() default "|";
}
