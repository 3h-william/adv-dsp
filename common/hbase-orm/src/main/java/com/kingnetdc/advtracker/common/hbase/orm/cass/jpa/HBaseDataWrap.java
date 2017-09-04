package com.kingnetdc.advtracker.common.hbase.orm.cass.jpa;

import org.apache.hadoop.hbase.client.Put;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by william on 2017/5/23.
 */
public class HBaseDataWrap {

    private String tableName;
    private byte[] key;
    private List<HBaseColumnValue> columnValues;

    public HBaseDataWrap(String tableName, byte[] key, List<HBaseColumnValue> columnValues) {
        this.tableName = tableName;
        this.key = key;
        this.columnValues = columnValues;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public List<HBaseColumnValue> getColumnValues() {
        return columnValues;
    }

    public void setColumnValues(List<HBaseColumnValue> columnValues) {
        this.columnValues = columnValues;
    }
}
