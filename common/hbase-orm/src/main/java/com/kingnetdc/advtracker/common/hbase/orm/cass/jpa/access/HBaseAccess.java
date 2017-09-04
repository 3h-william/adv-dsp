package com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access;

import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.HBaseDataWrap;
import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.convert.HBaseObjectBinderBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 */
public class HBaseAccess implements HBaseAccessInterface {

    private final Connection connection;
    private final HBaseObjectBinderBean hBaseObjectBinderBean;
    private String nameSpace;

    public HBaseAccess(Connection connection) {
        this.connection = connection;
        this.hBaseObjectBinderBean = new HBaseObjectBinderBean();
    }

    public HBaseAccess(Connection connection, String nameSpace) {
        this.connection = connection;
        this.hBaseObjectBinderBean = new HBaseObjectBinderBean();
        this.nameSpace = nameSpace;
    }

    public void operate(HBaseOperate hBaseOperate, Table table) throws Exception {
        try {
            hBaseOperate.apply(table);
        } finally {
            if (null != table) {
                table.close();
            }
        }
    }

    public <T> T operateWithReturn(HBaseOperateWithReturn hBaseOperateWithReturn, Table table) throws Exception {
        try {
            return (T) hBaseOperateWithReturn.apply(table);
        } finally {
            if (null != table) {
                table.close();
            }
        }
    }

    public <T> String getTableName(Class<T> clazz) {
        return getTableName(hBaseObjectBinderBean.getColumnName(clazz));
    }

    public String getTableName(String tableName) {
        if (StringUtils.isBlank(nameSpace)) {
            return tableName;
        } else {
            return nameSpace + ":" + tableName;
        }
    }

    public <T> Table getTable(Class<T> clazz) throws IOException {
        return connection.getTable(TableName.valueOf(getTableName(clazz)));
    }

    public <T> Table getTable(String tableName) throws IOException {
        return connection.getTable(TableName.valueOf(getTableName(tableName)));
    }


    public <T> T getBean(Result result, Class<T> clazz) throws Exception {
        return hBaseObjectBinderBean.getBean(result, clazz);
    }


    @Override
    public void save(Object value) throws Exception {
        save(value, null);
    }

    public <T> void save(Class<T> clazz, List<T> values) throws Exception {
        save(clazz, values, null);
    }

    public <T> void save(Class<T> clazz, List<T> values, Long versionTimestamp) throws Exception {
        Table table = connection.getTable(TableName.valueOf(getTableName(clazz)));
        List<HBaseDataWrap> hBaseDataWraps = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            HBaseDataWrap hBaseDataWrap = hBaseObjectBinderBean.convertToKeyValue(values.get(i), null, versionTimestamp);
            hBaseDataWraps.add(hBaseDataWrap);
        }
        HBaseOperate saveOperates = (htable) -> {
            for (int i = 0; i < hBaseDataWraps.size(); i++) {
                HBaseDataWrap hBaseDataWrap = hBaseDataWraps.get(i);
                Put put = generatePut(hBaseDataWrap);
                htable.put(put);
            }
        };
        operate(saveOperates, table);
    }

    private Put generatePut(HBaseDataWrap hBaseDataWrap) {
        Put put = new Put(hBaseDataWrap.getKey());
        hBaseDataWrap.getColumnValues().forEach(hBaseColumnValue -> {
            put.addColumn(hBaseColumnValue.getFamily(), hBaseColumnValue.getQualifier(), hBaseColumnValue.getTimeStamp(), hBaseColumnValue.getValue());
        });
        return put;
    }


    public void saveWithTTL(Object value, Long ttl) throws Exception {
        HBaseDataWrap hBaseDataWrap = hBaseObjectBinderBean.convertToKeyValue(value, null, null);
        Table table = connection.getTable(TableName.valueOf(getTableName(hBaseDataWrap.getTableName())));
        HBaseOperate saveOperate = (htable) -> {
            Put put = generatePut(hBaseDataWrap);
            put.setTTL(ttl);
            htable.put(put);
        };
        operate(saveOperate, table);
    }

    public void save(Object value, Long versionTimestamp) throws Exception {
        HBaseDataWrap hBaseDataWrap = hBaseObjectBinderBean.convertToKeyValue(value, null, versionTimestamp);
        Table table = connection.getTable(TableName.valueOf(getTableName(hBaseDataWrap.getTableName())));
        HBaseOperate saveOperate = (htable) -> {
            Put put = generatePut(hBaseDataWrap);
            htable.put(put);
        };
        operate(saveOperate, table);
    }

    @Override
    public <T> T getByRowkey(Object rowkey, Class<T> clazz) throws Exception {
        Table table = getTable(clazz);
        try {
            Get get = new Get(hBaseObjectBinderBean.convertObjectToBytes(rowkey));
            Result result = table.get(get);
            return getBean(result, clazz);
        } finally {
            table.close();
        }
    }

    @Override
    public <T> List<T> getByRowkeys(List<Object> rowkeys, Class<T> clazz) throws Exception {
        throw new RuntimeException("not support yet");
    }

    @Override
    public void updateColumns(Object value, List<String> specifyColumnNames) throws Exception {
        throw new RuntimeException("not support yet");

    }

    @Override
    public void updateColumns(Object value, List<String> specifyColumnNames, Long timeStamp) throws Exception {
        throw new RuntimeException("not support yet");
    }

    @Override
    public void updateColumn(Object value, String specifyColumnName) throws Exception {
        throw new RuntimeException("not support yet");
    }

    @Override
    public void updateColumn(Object value, String specifyColumnName, Long timeStamp) throws Exception {
        Map<String, Integer> specifyColumnNameMap = null;
        if (null != specifyColumnName) {
            specifyColumnNameMap = new HashMap<String, Integer>();
            Integer tmpV = Integer.valueOf(1);
            // change list to map
            specifyColumnNameMap.put(specifyColumnName, tmpV);
        }
        HBaseDataWrap hBaseDataWrap = hBaseObjectBinderBean.convertToKeyValue(value, specifyColumnNameMap, timeStamp);
        save(hBaseDataWrap);
    }

    @Override
    public <T> void deleteByRowkey(Object rowkey, Class<T> clazz) throws Exception {
        throw new RuntimeException("not support yet");
    }

    @Override
    public <T> void deleteByRowkey(Object rowkey, Class<T> clazz, List<String> specifyColumnNames) throws Exception {
        throw new RuntimeException("not support yet");
    }

    @Override
    public <T> boolean isKeyExist(Object rowkey, Class<T> clazz) throws Exception {
        Table table = getTable(clazz);
        try {
            Get get = new Get(hBaseObjectBinderBean.convertObjectToBytes(rowkey));
            return table.exists(get);
        } finally {
            table.close();
        }
    }
}
