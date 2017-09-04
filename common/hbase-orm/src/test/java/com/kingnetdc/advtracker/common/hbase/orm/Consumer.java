package com.kingnetdc.advtracker.common.hbase.orm;

import java.util.function.Function;

@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}