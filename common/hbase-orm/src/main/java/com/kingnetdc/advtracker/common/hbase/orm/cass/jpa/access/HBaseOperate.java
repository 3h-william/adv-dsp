package com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access;

import org.apache.hadoop.hbase.client.Table;

@FunctionalInterface
public interface HBaseOperate {
    void apply(Table from) throws Exception;
}

