package com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access;

import org.apache.hadoop.hbase.client.Table;

@FunctionalInterface
public interface HBaseOperateWithReturn<T> {
    T apply(Table from) throws Exception;
}
