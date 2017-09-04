package com.kingnetdc.advtracker.common.hbase.orm.cass.jpa;

public class HBaseColumnValue {
    private byte[] family;
    private byte[] qualifier;
    private byte[] value;
    private Long timeStamp;

    public HBaseColumnValue(byte[] family, byte[] qualifier, byte[] value, Long timeStamp) {
        this.family = family;
        this.qualifier = qualifier;
        this.value = value;
        this.timeStamp = timeStamp;
    }

    public byte[] getFamily() {
        return family;
    }

    public void setFamily(byte[] family) {
        this.family = family;
    }

    public byte[] getQualifier() {
        return qualifier;
    }

    public void setQualifier(byte[] qualifier) {
        this.qualifier = qualifier;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

