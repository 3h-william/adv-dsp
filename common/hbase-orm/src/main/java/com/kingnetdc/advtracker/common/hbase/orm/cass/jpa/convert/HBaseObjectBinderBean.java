package com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.convert;

import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.HBaseColumnValue;
import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.HBaseDataWrap;
import com.kingnetdc.advtracker.common.hbase.orm.jpa.annotation.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe
 *
 * @author william
 */
public class HBaseObjectBinderBean {
    private final static Log log = LogFactory.getLog(HBaseObjectBinderBean.class);

    private final Map<Class<?>, HBaseFieldCollect> infocache = new ConcurrentHashMap<Class<?>, HBaseFieldCollect>();

    public HBaseFieldCollect getHBaseFields(Class<?> clazz) {
        HBaseFieldCollect hBaseFieldCollect = infocache.get(clazz);
        if (hBaseFieldCollect == null) {
            hBaseFieldCollect = collectInfo(clazz);
            infocache.put(clazz, hBaseFieldCollect);
        }
        return hBaseFieldCollect;
    }

    public <T> String getColumnName(Class<T> clazz) {
        HBaseFieldCollect hBaseFieldCollect = getHBaseFields(clazz);
        return hBaseFieldCollect.getTableName();
    }


//    /**
//     * get bufferList from Object List
//     *
//     * @return
//     * @throws IllegalArgumentException
//     * @throws Exception
//     */
//    public List<ByteBuffer> convertObjectListToByteBufferList(List<Object> objects) throws Exception {
//        List<ByteBuffer> byteBufferList = new ArrayList<ByteBuffer>();
//        ;
//        for (Object obj : objects) {
//            ByteBuffer byteBuffer = convertObjectToByteBuffer(obj);
//            if (null != byteBuffer) {
//                byteBufferList.add(byteBuffer);
//            }
//        }
//        return byteBufferList;
//    }

//    /**
//     * get an object from results
//     *
//     * @param <T>
//     * @param clazz
//     * @param results
//     * @return
//     * @throws Exception
//     */
//    public <T> T getBean(Class<T> clazz, Map<ByteBuffer, List<HCell>> results) throws Exception {
//        Set<Entry<ByteBuffer, List<HCell>>> entry = results.entrySet();
//        if (null == entry || entry.size() == 0)
//            return null;
//        T obj = null;
//        // loop parse canary return result
//        for (Entry<ByteBuffer, List<HCell>> ent : entry) {
//            obj = getBean(clazz, ent);
//            break; // one object must have one ent
//        }
//        return obj;
//    }

//    /**
//     * get an object from results
//     *
//     * @param <T>
//     * @param clazz
//     * @param results
//     * @return
//     * @throws Exception
//     */
//    public <T> List<T> getBeans(Class<T> clazz, Map<ByteBuffer, List<HCell>> results) throws Exception {
//        Set<Entry<ByteBuffer, List<HCell>>> entry = results.entrySet();
//        if (null == entry || entry.size() == 0)
//            return null;
//        List<T> objs = new ArrayList<T>();
//        // loop parse canary return result
//        for (Entry<ByteBuffer, List<HCell>> ent : entry) {
//            T obj = getBean(clazz, ent);
//            if (null != obj) {
//                objs.add(obj);
//            }
//        }
//        return objs;
//    }

    /**
     * get an object from Result
     *
     * @param <T>
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public <T> T getBean(Result result, Class<T> clazz) throws InstantiationException, IllegalArgumentException, Exception {
        if (null == result || null == result.getRow()) {
            return null;
        }
        // 1st, get class info
        HBaseFieldCollect hBaseFieldCollect = getHBaseFields(clazz);
        Map<String, Field> fieldsMap = hBaseFieldCollect.getFieldsMap();
        Field rowkeyField = hBaseFieldCollect.getRowkey();
        T obj = clazz.newInstance();// new instance
        Map<String, Field> dynamicFieldsMap = hBaseFieldCollect.getDynamicFieldsMap();
        Map<String, Type> dynamicGenericMap = hBaseFieldCollect.getDynamicGenericMap();

        // 2st, get all "prefixName + delimiter" collection in a String[]
        // all "prefixName + delimiter" collection
//        String[] dynamicFieldsArray = null;
//        if (null != dynamicFieldsMap && dynamicFieldsMap.size() != 0) {
//            dynamicFieldsArray = new String[dynamicFieldsMap.size()];
//            int pos = 0;
//            for (Entry<String, Field> dynamicFields : dynamicFieldsMap.entrySet()) {
//                dynamicFieldsArray[pos++] = dynamicFields.getKey();
//            }
//        }
        // 3rd, get and parse a rowkey object
        parse(rowkeyField, obj, result.getRow());
        // 4th, parse normal columns & dynamic columns
        // key=dynamicFieldsPrefix name,value=dynamic fields object in class
//        Map<String, List> dynamicListMap = null;

        fieldsMap.forEach((columnName, field) -> {
            String familyName = getFamilyName(field, hBaseFieldCollect.getDefaultFaimilyName());
            byte[] value = result.getValue(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
            try {
                parse(field, obj, value); //parse value and set field to new T
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
//        // 5th, dynamicList mapping to field
//        if (null != dynamicListMap || dynamicListMap.size() != 0) {
//            for (Entry<String, List> dMap : dynamicListMap.entrySet()) {
//                Field dyField = dynamicFieldsMap.get(dMap.getKey());
//                dyField.set(obj, dMap.getValue());
//            }
//        }
        return obj;
    }

    /**
     * @param clazz
     * @return
     */
    private HBaseFieldCollect collectInfo(Class<?> clazz) {
        HBaseTable hBaseTable = clazz.getAnnotation(HBaseTable.class);
        checkTableName(hBaseTable);  //check
        String hbaseTableName = hBaseTable.name();
        String defaultFamilyName = hBaseTable.defaultFamily();
        Class<?> superClazz = clazz;
        List<Field> members = new ArrayList<Field>();
        Field rowkey = null;
        Map<String, Field> fieldsMap = new HashMap<String, Field>();
        Map<String, Field> dynamicFieldsMap = null; // could be null
        Map<String, Type> dynamicGenericMap = null;
        // get all fields
        while (superClazz != null && superClazz != Object.class) {
            members.addAll(Arrays.asList(superClazz.getDeclaredFields()));
            superClazz = superClazz.getSuperclass();
        }
        for (Field field : members) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(HBaseColumn.class)) {
                fieldsMap.put(getColumnName(field), field);// fieldMap
            } else if (field.isAnnotationPresent(HBaseRowkey.class)) {
                if (null != rowkey)
                    throw new IllegalArgumentException("@HBaseRowkey Annotation may be defined more than once");
                rowkey = field;// rowkey

            }
//            // CassandraDynamicColumn info save in 2 map
//            else if (field.isAnnotationPresent(CassandraDynamicColumn.class)) {
//                if (null == dynamicFieldsMap) {
//                    dynamicFieldsMap = new HashMap<String, Field>();
//                    dynamicGenericMap = new HashMap<String, Type>();
//                }
//                Type listType = field.getGenericType();
//                Type[] dynamicType = ((ParameterizedType) listType).getActualTypeArguments();
//                Type[] actualType = ((ParameterizedType) dynamicType[0]).getActualTypeArguments();
//                dynamicGenericMap.put(getDynamicColumnName(field), actualType[0]);
//                dynamicFieldsMap.put(getDynamicColumnName(field), field);
//
//            }
        }
        return new HBaseFieldCollect(hbaseTableName, defaultFamilyName, rowkey, fieldsMap, dynamicFieldsMap, dynamicGenericMap);
    }

    /**
     * Convert a dto to HBaseDataWrap
     *
     * @throws Exception
     */
    public HBaseDataWrap convertToKeyValue(Object value, Map<String, Integer> specifyColumnNameMap) throws Exception {
        return convertToKeyValue(value, specifyColumnNameMap, null);
    }

    /**
     * Convert a dto to HBaseDataWrap
     *
     * @throws Exception
     */
    public HBaseDataWrap convertToKeyValue(Object value, Map<String, Integer> specifyColumnNameMap, long timestamp) throws Exception {
        return convertToKeyValue(value, specifyColumnNameMap, timestamp);
    }


    /**
     * Convert a dto to HBaseDataWrap
     *
     * @param value
     * @param specifyColumnNameMap
     * @param timeStamp
     * @return
     * @throws Exception
     */
    public HBaseDataWrap convertToKeyValue(Object value, Map<String, Integer> specifyColumnNameMap, Long timeStamp) throws Exception {
        HBaseTable hBaseTable = value.getClass().getAnnotation(HBaseTable.class);
        String defaultFamilyName = hBaseTable.defaultFamily();
        checkTableName(hBaseTable); //check folum family value
        //checkColumnFamily(cfamily); //check folum family value
        List<HBaseColumnValue> columnValues = new ArrayList();
        Field[] fields = new Field[0];//value.getClass().getDeclaredFields();
        Class<?> superClass = value.getClass();
        // get all superclass fields
        while (superClass != Object.class) {
            fields = (Field[]) ArrayUtils.addAll(fields, superClass.getDeclaredFields());
            superClass = superClass.getSuperclass();
        }
        long now = null == timeStamp ? getCurrentTime() : timeStamp;
        byte[] key = null;
        // find annotation and construct cassandra mutation
        for (Field field : fields) {
            field.setAccessible(true);
            if (null == field.get(value)) continue;// check null first
            // 1st find HBaseRowkey type
            if (field.isAnnotationPresent(HBaseRowkey.class)) {
                if (null != key) {
                    throw new IllegalArgumentException("rowKey annotation is duplicated");
                }
                key = convertObjectToBytes(field.get(value));// convert
            }
            // 2nd find HBaseColumn type
            else if (field.isAnnotationPresent(HBaseColumn.class)) {
                convertHBaseColumnType(value, columnValues, now, field, specifyColumnNameMap, defaultFamilyName);
            }
//            // 3rd find CassandraDynamicColumn type
//            else if (field.isAnnotationPresent(CassandraDynamicColumn.class)) {
//                convertCassandraDynamicColumnType(value, mutations, now, field, specifyColumnNameMap);
//            }
        }
        // make sure key,cfamily,mutations are not present
        checkValue(key, columnValues);
        if (columnValues.size() == 0) {
            log.warn("mutations size = 0 ");
        }
        return new HBaseDataWrap(hBaseTable.name(), key, columnValues);
    }

    /**
     * check folum family value
     */
    private void checkTableName(HBaseTable hBaseTable) {
        if (null == hBaseTable || hBaseTable.name().equals("")) {
            throw new IllegalArgumentException("the annotation @HBaseTable is not exist ");
        }
    }

    /**
     * cassandra timestamp is 16 chars with long type
     *
     * @return
     */
    private long getCurrentTime() {
        long now = System.currentTimeMillis();
        return now;
    }

//    public CassKeyValue getDeletionKeyValue(ByteBuffer key, Class<?> clazz, List<String> specifyColumnNames) {
//        CassandraFamily cfamily = clazz.getAnnotation(CassandraFamily.class);
//        checkColumnFamily(cfamily); //check folum family value
//        List<Mutation> deleteMutations = getDeletionMutationsByColums(specifyColumnNames);
//        return new CassKeyValue(cfamily.name(), key, deleteMutations);
//    }


    /**
     * if true, go on
     * <p>
     * if false, skip
     *
     * @param specifyColumnNameMap
     * @param specifyName
     * @return
     */
    private boolean checkSpecifyColumn(Map<String, Integer> specifyColumnNameMap, String specifyName) {
        if (null != specifyColumnNameMap) {
            if (!specifyColumnNameMap.containsKey(specifyName)) {
                return false;// not convert to mutation
            }
        }
        return true;
    }

    private void convertHBaseColumnType(Object value, List<HBaseColumnValue> columnValues, long now, Field field, Map<String, Integer> specifyColumnNameMap, String defaultFamilyName)
            throws Exception {
        byte[] columnValue;
        String columnName = getColumnName(field);
        String familyName = getFamilyName(field, defaultFamilyName);
        if (StringUtils.isBlank(familyName)) {
            throw new IllegalArgumentException("familyName is empty , columnName : " + columnName);
        }
        if (!checkSpecifyColumn(specifyColumnNameMap, columnName)) {
            return;
        }
        columnValue = convertObjectToBytes(field.get(value));// convert
        // skip convert if columnValue is null
        if (null != columnValue) {
            columnValues.add(new HBaseColumnValue(Bytes.toBytes(familyName), Bytes.toBytes(columnName), columnValue, now));
        }
    }

    /**
     * get column name mapping with @HBaseColumn
     * <p>
     * default is field.getName();
     *
     * @param field
     * @return
     */
    private String getColumnName(Field field) {
        HBaseColumn hcolumn = field.getAnnotation(HBaseColumn.class);
        String columnName = null;
        // use field name if CassandraColumn dose not define
        if (!hcolumn.name().equals("")) {
            columnName = hcolumn.name();
        } else {
            columnName = field.getName();
        }
        return columnName;
    }

    /**
     * get faimily name mapping with @HBaseColumn
     * <p>
     * default is field.getName();
     *
     * @param field
     * @return
     */
    private String getFamilyName(Field field, String defaultFamilyName) {
        HBaseFamily hBaseFamily = field.getAnnotation(HBaseFamily.class);
        String familyName = null;
        // use field name if CassandraColumn dose not define
        if (null != hBaseFamily && !hBaseFamily.name().equals("")) {
            familyName = hBaseFamily.name();
        } else {
            familyName = defaultFamilyName;
        }
        return familyName;
    }


    private void checkDynamicName(String prefixName, String delimiter) {
        if (null == prefixName || prefixName.equals(""))
            throw new IllegalArgumentException("prefixName is not present " + prefixName);
        if (null == delimiter || delimiter.equals(""))
            throw new IllegalArgumentException("delimiter is not present " + delimiter);
    }


    /**
     * check parameters
     *
     * @param key
     * @param columnValues
     * @throws IllegalArgumentException
     */
    private void checkValue(byte[] key, List<HBaseColumnValue> columnValues) throws IllegalArgumentException {
        if (null == key) {
            throw new IllegalArgumentException("key is null " + key);
        }
        if (columnValues == null) {
            throw new IllegalArgumentException("columnValues is null" + columnValues);
        }
    }

    /**
     * get byte[] from value which get from field
     *
     * @param value
     * @return
     * @throws IllegalArgumentException
     */
    public byte[] convertObjectToBytes(Object value) throws IllegalArgumentException {
        if (null == value)
            return null;
        else if (value.getClass().equals(Long.class) || value.getClass().equals(long.class)) {
            return Bytes.toBytes((Long) value);
        } else if (value.getClass().equals(Integer.class) || value.getClass().equals(int.class)) {
            return Bytes.toBytes((Integer) value);
        } else if (value.getClass().equals(Boolean.class) || value.getClass().equals(boolean.class)) {
            return Bytes.toBytes((Boolean) value);
        } else if (value.getClass().equals(String.class)) {
            String fieldValue = (String) (value);
            return Bytes.toBytes(fieldValue);
        } else if (value.getClass().equals(byte[].class)) {
            return ((byte[]) value);
        } else {
            throw new IllegalArgumentException("no match type:" + value.getClass());
        }
    }

    /**
     * parse an object,and fill obj with field and value
     *
     * @param field
     * @param obj
     * @param value
     * @throws IllegalAccessException
     * @throws Exception
     */
    private void parse(Field field, Object obj, byte[] value) throws IllegalArgumentException, IllegalAccessException {
        if (null == value || null == field)
            return;
        if (field.getType().equals(int.class)) {
            field.setInt(obj, Bytes.toInt(value));
        } else if (field.getType().equals(Integer.class)) {
            field.set(obj, Bytes.toInt(value));
        } else if (field.getType().equals(long.class)) {
            field.setLong(obj, Bytes.toLong(value));
        } else if (field.getType().equals(Long.class)) {
            field.set(obj, Bytes.toLong(value));
        } else if (field.getType().equals(Boolean.class)) {
            field.set(obj, Bytes.toBoolean(value));
        } else if (field.getType().equals(String.class)) {
            field.set(obj, Bytes.toString(value));
        } else {
            throw new IllegalArgumentException("no match type:" + field.getType());
        }
    }

    /**
     * @param type
     * @param value
     * @return
     * @throws Exception
     */
    private Object newObject(Type type, byte[] value) throws Exception {
        if (type.equals(Integer.class) || type.equals(int.class)) {
            return Bytes.toInt(value);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return Bytes.toLong(value);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return Bytes.toLong(value);
        } else if (type.equals(String.class)) {
            return Bytes.toString(value);
        } else {
            throw new IllegalArgumentException("no match type:" + type);
        }
    }

    /**
     * a class collect entity's mapping with cassandra annotation
     */
    private class HBaseFieldCollect {
        private String tableName;
        private String defaultFaimilyName;
        private Field rowkey;
        private Map<String, Field> fieldsMap;
        // key is "prefixName + delimiter",value is field map with Object
        private Map<String, Field> dynamicFieldsMap;
        // key is "prefixName + delimiter",value is type eg.
        // List<CassDynamicType<String>> is "String Type"
        private Map<String, Type> dynamicGenericMap;

        private HBaseFieldCollect() {
        }

        public HBaseFieldCollect(String tableName, String defaultFaimilyName, Field rowkey, Map<String, Field> fieldsMap,
                                 Map<String, Field> dynamicFieldsMap, Map<String, Type> dynamicGenericMap) {
            super();
            this.tableName = tableName;
            this.defaultFaimilyName = defaultFaimilyName;
            this.rowkey = rowkey;
            this.fieldsMap = fieldsMap;
            this.dynamicFieldsMap = dynamicFieldsMap;
            this.dynamicGenericMap = dynamicGenericMap;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public Field getRowkey() {
            return rowkey;
        }

        public void setRowkey(Field rowkey) {
            this.rowkey = rowkey;
        }

        public Map<String, Field> getFieldsMap() {
            return fieldsMap;
        }

        public void setFieldsMap(Map<String, Field> fieldsMap) {
            this.fieldsMap = fieldsMap;
        }

        public Map<String, Field> getDynamicFieldsMap() {
            return dynamicFieldsMap;
        }

        public void setDynamicFieldsMap(Map<String, Field> dynamicFieldsMap) {
            this.dynamicFieldsMap = dynamicFieldsMap;
        }

        public Map<String, Type> getDynamicGenericMap() {
            return dynamicGenericMap;
        }

        public void setDynamicGenericMap(Map<String, Type> dynamicGenericMap) {
            this.dynamicGenericMap = dynamicGenericMap;
        }

        public String getDefaultFaimilyName() {
            return defaultFaimilyName;
        }

        public void setDefaultFaimilyName(String defaultFaimilyName) {
            this.defaultFaimilyName = defaultFaimilyName;
        }
    }

}