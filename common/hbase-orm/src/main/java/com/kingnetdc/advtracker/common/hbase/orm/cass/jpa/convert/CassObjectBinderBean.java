//package com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.convert;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.Map.Entry;
//import java.util.concurrent.ConcurrentHashMap;
//
//import me.prettyprint.cassandra.serializers.ByteBufferSerializer;
//import me.prettyprint.cassandra.serializers.IntegerSerializer;
//import me.prettyprint.cassandra.serializers.LongSerializer;
//import me.prettyprint.cassandra.serializers.StringSerializer;
//
//import org.apache.cassandra.thrift.Column;
//import org.apache.cassandra.thrift.ColumnOrSuperColumn;
//import org.apache.cassandra.thrift.Deletion;
//import org.apache.cassandra.thrift.Mutation;
//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import com.newegg.ec.canary.thrift.HCell;
//import com.newegg.ec.canary.thrift.SlicePredicate;
//import com.newegg.ec.canary.thrift.SliceRange;
//import com.newegg.ec.service.qa.orm.cass.jpa.CassDynamicType;
//import com.newegg.ec.service.qa.orm.cass.jpa.CassKeyValue;
//import com.newegg.ec.service.qa.orm.cass.jpa.CassSlicePredicate;
//import com.newegg.ec.service.qa.orm.cass.jpa.annotation.CassandraColumn;
//import com.newegg.ec.service.qa.orm.cass.jpa.annotation.CassandraDynamicColumn;
//import com.newegg.ec.service.qa.orm.cass.jpa.annotation.CassandraFamily;
//import com.newegg.ec.service.qa.orm.cass.jpa.annotation.CassandraRowkey;
//
///**
// *
// * do some convert with Entity and Cassandra's Mutation
// *
// * @author wz68
// *
// */
//public class CassObjectBinderBean {
//	private final static Log log = LogFactory.getLog(CassObjectBinderBean.class);
//	/**
//	 * every types should be converted with hector'serialization
//	 */
//	private StringSerializer stringSerializer = StringSerializer.get();
//	private LongSerializer longSerializer = LongSerializer.get();
//	private IntegerSerializer integerSerializer = IntegerSerializer.get();
//	private ByteBufferSerializer byteBufferSerializer = ByteBufferSerializer.get();
//	// private BytesArraySerializer byteArraySerializer;
//	// private BooleanSerializer booleanSerializer;
//
//	private final Map<Class<?>, CassFieldCollect> infocache = new ConcurrentHashMap<Class<?>, CassFieldCollect>();
//
//	public CassFieldCollect getCassFields(Class<?> clazz) {
//		CassFieldCollect cassFieldCollect = infocache.get(clazz);
//		if (cassFieldCollect == null) {
//			cassFieldCollect = collectInfo(clazz);
//			infocache.put(clazz, cassFieldCollect);
//		}
//		return cassFieldCollect;
//	}
//
//	/**
//	 * get bufferList from Object
//	 *
//	 * @param rowkey
//	 * @return
//	 * @throws IllegalAccessException
//	 * @throws Exception
//	 */
//	public List<ByteBuffer> convertObjectToByteBufferList(Object rowkey) throws Exception {
//		List<ByteBuffer> rowkeyList = new ArrayList<ByteBuffer>();;
//		ByteBuffer byteBuffer = convertObjectToByteBuffer(rowkey);
//		if (null != byteBuffer) {
//			rowkeyList.add(byteBuffer);
//		}
//		return rowkeyList;
//	}
//
//	/**
//	 * get bufferList from Object List
//	 *
//	 * @param rowkey
//	 * @return
//	 * @throws IllegalArgumentException
//	 * @throws Exception
//	 */
//	public List<ByteBuffer> convertObjectListToByteBufferList(List<Object> objects) throws Exception {
//		List<ByteBuffer> byteBufferList = new ArrayList<ByteBuffer>();;
//		for (Object obj : objects) {
//			ByteBuffer byteBuffer = convertObjectToByteBuffer(obj);
//			if (null != byteBuffer) {
//				byteBufferList.add(byteBuffer);
//			}
//		}
//		return byteBufferList;
//	}
//
//	/**
//	 * get an object from results
//	 *
//	 * @param <T>
//	 * @param clazz
//	 * @param results
//	 * @return
//	 * @throws Exception
//	 */
//	public <T> T getBean(Class<T> clazz, Map<ByteBuffer, List<HCell>> results) throws Exception {
//		Set<Entry<ByteBuffer, List<HCell>>> entry = results.entrySet();
//		if (null == entry || entry.size() == 0)
//			return null;
//		T obj = null;
//		// loop parse canary return result
//		for (Entry<ByteBuffer, List<HCell>> ent : entry) {
//			obj = getBean(clazz, ent);
//			break; // one object must have one ent
//		}
//		return obj;
//	}
//
//	/**
//	 * get an object from results
//	 *
//	 * @param <T>
//	 * @param clazz
//	 * @param results
//	 * @return
//	 * @throws Exception
//	 */
//	public <T> List<T> getBeans(Class<T> clazz, Map<ByteBuffer, List<HCell>> results) throws Exception {
//		Set<Entry<ByteBuffer, List<HCell>>> entry = results.entrySet();
//		if (null == entry || entry.size() == 0)
//			return null;
//		List<T> objs = new ArrayList<T>();
//		// loop parse canary return result
//		for (Entry<ByteBuffer, List<HCell>> ent : entry) {
//			T obj = getBean(clazz, ent);
//			if(null!=obj){
//				objs.add(obj);
//			}
//		}
//		return objs;
//	}
//
//	/**
//	 * get an object from Entry<ByteBuffer, List<HCell>>
//	 *
//	 * @param <T>
//	 * @param clazz
//	 * @param ent
//	 * @return
//	 * @throws InstantiationException
//	 * @throws IllegalArgumentException
//	 * @throws Exception
//	 */
//	private <T> T getBean(Class<T> clazz, Entry<ByteBuffer, List<HCell>> ent) throws InstantiationException, IllegalArgumentException, Exception {
//		// 1st, get class info
//		CassFieldCollect cassfields = getCassFields(clazz);
//		Map<String, Field> fieldsMap = cassfields.getFieldsMap();
//		Field rowkeyField = cassfields.getRowkey();
//		T obj = clazz.newInstance();// new instance
//		Map<String, Field> dynamicFieldsMap = cassfields.getDynamicFieldsMap();
//		Map<String, Type> dynamicGenericMap = cassfields.getDynamicGenericMap();
//
//		// 2st, get all "prefixName + delimiter" collection in a String[]
//		// all "prefixName + delimiter" collection
//		String[] dynamicFieldsArray = null;
//		if (null != dynamicFieldsMap && dynamicFieldsMap.size() != 0) {
//			dynamicFieldsArray = new String[dynamicFieldsMap.size()];
//			int pos = 0;
//			for (Entry<String, Field> dynamicFields : dynamicFieldsMap.entrySet()) {
//				dynamicFieldsArray[pos++] = dynamicFields.getKey();
//			}
//		}
//		// 3rd, get and parse a rowkey object
//		parse(rowkeyField, obj, ent.getKey().array());
//
//		// 4th, parse normal columns & dynamic columns
//		// key=dynamicFieldsPrefix name,value=dynamic fields object in class
//		Map<String, List> dynamicListMap = null;
//		List<HCell> result = ent.getValue();
//		/*
//		 * canary may return a rowkey with null colums if rowkey not exist,so we
//		 * should return null if it happends
//		 */
//		if (null == result || result.size() == 0)
//			return null;
//		for (HCell cell : result) {
//			String value = new String(cell.getColumn());
//			boolean isDynamic = false;
//			// it means that if has dynamic fields or not
//			if (null != dynamicFieldsArray) {
//				if (null == dynamicListMap) {
//					dynamicListMap = new HashMap<String, List>();
//				}
//				// loop to find dynamic fields
//				for (String dynamicFieldsPrefix : dynamicFieldsArray) {
//					if (StringUtils.startsWith(value, dynamicFieldsPrefix)) {// find
//						List dynamicLists = dynamicListMap.get(dynamicFieldsPrefix);
//						if (null == dynamicLists) {
//							dynamicLists = new ArrayList<CassDynamicType>();
//							dynamicListMap.put(dynamicFieldsPrefix, dynamicLists);
//						}
//						CassDynamicType cdtObj = new CassDynamicType();
//						cdtObj.setSuffixColumnName(StringUtils.substringAfter(value, dynamicFieldsPrefix));
//						Type type = dynamicGenericMap.get(dynamicFieldsPrefix);
//						cdtObj.setValue(newObject(type, cell.getValue()));
//						dynamicLists.add(cdtObj);
//						isDynamic = true;
//					}
//				}
//			}
//			if (!isDynamic) {
//				Field field = fieldsMap.get(value);
//				parse(field, obj, cell.getValue()); //parse value and set field to new T
//			}
//		}
//
//
//		// 5th, dynamicList mapping to field
//		if (null != dynamicListMap || dynamicListMap.size() != 0) {
//			for (Entry<String, List> dMap : dynamicListMap.entrySet()) {
//				Field dyField = dynamicFieldsMap.get(dMap.getKey());
//				dyField.set(obj, dMap.getValue());
//			}
//		}
//
//		return obj;
//	}
//
//	/**
//	 * get Column name from cache with clazz type
//	 *
//	 * Not Very Good impl, Should Be changed
//	 *
//	 * @param <T>
//	 * @param clazz
//	 * @return
//	 */
//	public <T> String getColumnName(Class<T> clazz) {
//		CassFieldCollect cassfields = getCassFields(clazz);
//		return cassfields.getColumnFamilyName();
//	}
//
//	/**
//	 *
//	 * @param clazz
//	 * @return
//	 */
//	private CassFieldCollect collectInfo(Class<?> clazz) {
//		CassandraFamily cfamily = clazz.getAnnotation(CassandraFamily.class);
//		checkColumnFamily(cfamily);  //check folum family value
//		String cfamilyName = cfamily.name();// cfamilyName
//		Class<?> superClazz = clazz;
//		List<Field> members = new ArrayList<Field>();
//		Field rowkey = null;
//		Map<String, Field> fieldsMap = new HashMap<String, Field>();
//		Map<String, Field> dynamicFieldsMap = null; // could be null
//		Map<String, Type> dynamicGenericMap = null;
//		// get all fields
//		while (superClazz != null && superClazz != Object.class) {
//			members.addAll(Arrays.asList(superClazz.getDeclaredFields()));
//			superClazz = superClazz.getSuperclass();
//		}
//		for (Field field : members) {
//			field.setAccessible(true);
//			if (field.isAnnotationPresent(CassandraColumn.class)) {
//				fieldsMap.put(getColumnName(field), field);// fieldMap
//			} else if (field.isAnnotationPresent(CassandraRowkey.class)) {
//				if (null != rowkey)
//					throw new IllegalArgumentException("@CassandraRowkey Annotation may be defined more than once");
//				rowkey = field;// rowkey
//				// CassandraDynamicColumn info save in 2 map
//			} else if (field.isAnnotationPresent(CassandraDynamicColumn.class)) {
//				if (null == dynamicFieldsMap) {
//					dynamicFieldsMap = new HashMap<String, Field>();
//					dynamicGenericMap = new HashMap<String, Type>();
//				}
//				Type listType = field.getGenericType();
//				Type[] dynamicType = ((ParameterizedType) listType).getActualTypeArguments();
//				Type[] actualType = ((ParameterizedType) dynamicType[0]).getActualTypeArguments();
//				dynamicGenericMap.put(getDynamicColumnName(field), actualType[0]);
//				dynamicFieldsMap.put(getDynamicColumnName(field), field);
//
//			}
//		}
//		return new CassFieldCollect(cfamilyName, rowkey, fieldsMap, dynamicFieldsMap, dynamicGenericMap);
//	}
//
//	/**
//	 * Convert a dto to mutation
//	 *
//	 * @param <T>
//	 * @param value
//	 * @param rowkey
//	 * @param clazz
//	 * @return
//	 * @throws Exception
//	 */
//	public CassKeyValue convertToKeyValue(Object value,Map<String,Integer> specifyColumnNameMap) throws Exception {
//		return convertToKeyValue(value, specifyColumnNameMap,null);
//	}
//
//
//	/**
//	 * Convert a dto to mutation
//	 *
//	 * @param <T>
//	 * @param value
//	 * @param rowkey
//	 * @param clazz
//	 * @return
//	 * @throws Exception
//	 */
//	public CassKeyValue convertToKeyValue(Object value,Map<String,Integer> specifyColumnNameMap,Long timeStamp) throws Exception {
//		CassandraFamily cfamily = value.getClass().getAnnotation(CassandraFamily.class);
//		checkColumnFamily(cfamily); //check folum family value
//		List<Mutation> mutations = new ArrayList<Mutation>();
//		Field[] fields = value.getClass().getDeclaredFields();
//		Class<?> superClass = value.getClass();
//		// get all superclass fields
//		while (superClass != Object.class) {
//			fields = (Field[]) ArrayUtils.addAll(fields, superClass.getDeclaredFields());
//			superClass = superClass.getSuperclass();
//		}
//		long now = null == timeStamp ? getCurrentTime() : timeStamp * 1000;
//		ByteBuffer key = null;
//		// find annotation and construct cassandra mutation
//		for (Field field : fields) {
//			field.setAccessible(true);
//			if(null==field.get(value)) continue;// check null first
//			// 1st find CassandraRowkey type
//			if (field.isAnnotationPresent(CassandraRowkey.class)) {
//				key = convertObjectToByteBuffer(field.get(value));// convert
//			}
//			// 2nd find CassandraColumn type
//			else if (field.isAnnotationPresent(CassandraColumn.class)) {
//				convertCassandraColumnType(value, mutations, now, field,specifyColumnNameMap);
//			}
//			// 3rd find CassandraDynamicColumn type
//			else if (field.isAnnotationPresent(CassandraDynamicColumn.class)) {
//				convertCassandraDynamicColumnType(value, mutations, now, field,specifyColumnNameMap);
//			}
//		}
//		// make sure key,cfamily,mutations are not present
//		checkValue(key, mutations);
//		if(mutations.size()==0){
//			log.warn("mutations size = 0 ");
//		}
//		return new CassKeyValue(cfamily.name(), key, mutations);
//	}
//
//	/**
//	 *  check folum family value
//	 *
//	 * @param cfamily
//	 */
//	private void checkColumnFamily(CassandraFamily cfamily) {
//		if (null == cfamily || cfamily.name().equals("")) {
//			throw new IllegalArgumentException("the annotation @CassandraFamily is not present ");
//		}
//	}
//
//	/**
//	 *  cassandra timestamp is 16 chars with long type
//	 * @return
//	 */
//	private long getCurrentTime() {
//		long now = System.currentTimeMillis()*1000;
//		return now;
//	}
//
//	private List<Mutation> getDeletionMutationsByColums(List<String> specifyColumnNames) {
//		List<Mutation> mutations = new ArrayList<Mutation>();
//		org.apache.cassandra.thrift.SlicePredicate delPred = new org.apache.cassandra.thrift.SlicePredicate();
//		Deletion deletion = new Deletion();
//		if (null == specifyColumnNames || specifyColumnNames.size() == 0) {
//			deletion.setSuper_columnIsSet(false);
//		} else {
//			for (String specifyColumnName : specifyColumnNames) {
//				List<ByteBuffer> delColumns = new ArrayList<ByteBuffer>();
//				delColumns.add(StringSerializer.get().toByteBuffer(specifyColumnName));
//				delPred.setColumn_names(delColumns);
//			}
//			deletion.setPredicate(delPred);
//		}
//		deletion.setTimestamp(getCurrentTime());
//		Mutation mutation = new Mutation();
//		mutation.setDeletion(deletion);
//		mutations.add(mutation);
//		return mutations;
//	}
//
//	public CassKeyValue getDeletionKeyValue(ByteBuffer key,Class<?> clazz,List<String> specifyColumnNames){
//		CassandraFamily cfamily = clazz.getAnnotation(CassandraFamily.class);
//		checkColumnFamily(cfamily); //check folum family value
//		List<Mutation> deleteMutations=getDeletionMutationsByColums(specifyColumnNames);
//		return new CassKeyValue(cfamily.name(), key, deleteMutations);
//	}
//
//	private void convertCassandraDynamicColumnType(Object value, List<Mutation> mutations, long now,
//			Field field,Map<String,Integer> specifyColumnNameMap) throws Exception {
//		// check if the field type is List or not
//		if (field.getType().equals(List.class)) {
//			CassandraDynamicColumn ccolumn = field.getAnnotation(CassandraDynamicColumn.class);
//			String prefixName = ccolumn.prefixName();
//			String delimiter = ccolumn.delimiter();
//			String preName = prefixName + delimiter;
//			checkDynamicName(prefixName, delimiter);
//			//check is specify column or not
//			if(!checkSpecifyColumn(specifyColumnNameMap, preName)){
//				return;
//			}
//			// get all CassDynamicType and convert type to mutation
//			List<?> DynamicList = (List<?>) field.get(value);
//			if(null!=DynamicList){
//				for (Object obj : DynamicList) {
//					CassDynamicType<?> cdt = (CassDynamicType<?>) obj;
//					String suffixName = cdt.getSuffixColumnName();// suffixColumnName
//					if (null == suffixName || suffixName.equals("")) {
//						throw new IllegalArgumentException("suffixName is not present " + suffixName);
//					}
//					String columnName = preName + suffixName;
//					ByteBuffer dynamicColumnValue = convertObjectToByteBuffer(cdt.getValue());// convert
//					mutations.add(generateColumnMutation(now, dynamicColumnValue, columnName));
//				}
//			}
//		} else {
//			throw new IllegalArgumentException("CassandraDynamicColumn must cast to List<CassDynamicType>");
//		}
//	}
//
//	/**
//	 * if true, go on
//	 *
//	 * if false, skip
//	 *
//	 * @param specifyColumnNameMap
//	 * @param specifyName
//	 * @return
//	 */
//	private boolean checkSpecifyColumn(Map<String, Integer> specifyColumnNameMap, String specifyName) {
//		if(null!=specifyColumnNameMap){
//			if(!specifyColumnNameMap.containsKey(specifyName)){
//				return false;// not convert to mutation
//			}
//		}
//		return true;
//	}
//
//	private void convertCassandraColumnType(Object value, List<Mutation> mutations, long now, Field field,Map<String,Integer> specifyColumnNameMap)
//		throws IllegalArgumentException, Exception {
//		ByteBuffer columnValue;
//		String columnName = getColumnName(field);
//		if(!checkSpecifyColumn(specifyColumnNameMap, columnName)){
//			return;
//		}
//		columnValue = convertObjectToByteBuffer(field.get(value));// convert
//		// skip convert if columnValue is null
//		if (null != columnValue) {
//			mutations.add(generateColumnMutation(now, columnValue, columnName));
//		}
//	}
//
//	/**
//	 * type SlicePredicate is canary type
//	 *
//	 * @param cassSlicePredicate
//	 * @return
//	 * @throws Exception
//	 */
//	public SlicePredicate convertSpecifyRestrict(CassSlicePredicate cassSlicePredicate) throws Exception {
//		SlicePredicate slicePredicate = new SlicePredicate();
//		if (null != cassSlicePredicate) {
//			// set SliceRange
//			if (null != cassSlicePredicate.getCassSliceRange()) {
//				SliceRange slice_range = new SliceRange();
//				slice_range.count = cassSlicePredicate.getCassSliceRange().getCount();
//				slice_range.reversed = false;
//				slice_range.start = convertObjectToByteBuffer(cassSlicePredicate.getCassSliceRange().getStart());
//				slice_range.finish = convertObjectToByteBuffer(cassSlicePredicate.getCassSliceRange().getFinish());
//				slicePredicate.setSlice_range(slice_range);
//			}
//			// set colunmNames;
//			if (null != cassSlicePredicate.getColumn_names()) {
//				slicePredicate.setColumn_names(convertObjectListToByteBufferList(cassSlicePredicate.getColumn_names()));
//			}
//		}
//		return slicePredicate;
//	}
//
//	/**
//	 * get column name mapping with @CassandraColumn
//	 *
//	 * default is field.getName();
//	 *
//	 * @param field
//	 * @return
//	 */
//	private String getColumnName(Field field) {
//		CassandraColumn ccolumn = field.getAnnotation(CassandraColumn.class);
//		String columnName = null;
//		// use field name if CassandraColumn dose not define
//		if (!ccolumn.name().equals("")) {
//			columnName = ccolumn.name();
//		} else {
//			columnName = field.getName();
//		}
//		return columnName;
//	}
//
//	/**
//	 * get dynamic column name mapping with @CassandraDynamicColumn
//	 *
//	 * return is prefixName + delimiter;
//	 *
//	 * @param field
//	 * @return
//	 */
//	private String getDynamicColumnName(Field field) {
//		CassandraDynamicColumn dynamiColumn = field.getAnnotation(CassandraDynamicColumn.class);
//		String prefixName = dynamiColumn.prefixName();
//		String delimiter = dynamiColumn.delimiter();
//		checkDynamicName(prefixName, delimiter);
//		return prefixName + delimiter;
//	}
//
//	private void checkDynamicName(String prefixName, String delimiter) {
//		if (null == prefixName || prefixName.equals(""))
//			throw new IllegalArgumentException("prefixName is not present " + prefixName);
//		if (null == delimiter || delimiter.equals(""))
//			throw new IllegalArgumentException("delimiter is not present " + delimiter);
//	}
//
//	/**
//	 * generate column mutation
//	 *
//	 * @param now
//	 * @param columnValue
//	 * @param columnName
//	 * @return
//	 */
//	private Mutation generateColumnMutation(long now, ByteBuffer columnValue, String columnName) {
//		Column column = new Column();
//		column.setName(stringSerializer.toByteBuffer(columnName));
//		column.setValue(columnValue);
//		column.setTimestamp(now);
//		ColumnOrSuperColumn column_or_supercolumn = new ColumnOrSuperColumn();
//		column_or_supercolumn.setColumn(column);
//		Mutation mutation = new Mutation();
//		mutation.setColumn_or_supercolumn(column_or_supercolumn);
//		return mutation;
//	}
//
//	/**
//	 *
//	 * check parameters
//	 *
//	 * @param key
//	 * @param cfamily
//	 * @param mutations
//	 * @throws Exception
//	 */
//	private void checkValue(ByteBuffer key, List<Mutation> mutations) throws IllegalArgumentException {
//		if (null == key) {
//			throw new IllegalArgumentException("key is null " + key);
//		}
//		if (mutations == null) {
//			throw new IllegalArgumentException("mutations is null" + mutations);
//		}
//	}
//
//	/**
//	 *
//	 * get ByteBuffer from value which get from field
//	 *
//	 * @param value
//	 * @return
//	 * @throws IllegalArgumentException
//	 */
//	public ByteBuffer convertObjectToByteBuffer(Object value) throws IllegalArgumentException {
//		if (null == value)
//			return null;
//		else if (value.getClass().equals(Long.class) || value.getClass().equals(long.class)) {
//			return longSerializer.toByteBuffer((Long) value);
//		} else if (value.getClass().equals(Integer.class) || value.getClass().equals(int.class)) {
//			return integerSerializer.toByteBuffer((Integer) value);
//		} else if (value.getClass().equals(String.class)) {
//			String fieldValue = (String) (value);
//			return stringSerializer.toByteBuffer(fieldValue);
//		} else if (value.getClass().equals(byte[].class)) {
//			return byteBufferSerializer.fromBytes((byte[]) value);
//		} else {
//			throw new IllegalArgumentException("no match type:" + value.getClass());
//		}
//	}
//
//	/**
//	 * parse an object,and fill obj with field and value
//	 *
//	 * @param field
//	 * @param obj
//	 * @param value
//	 * @throws IllegalAccessException
//	 * @throws Exception
//	 */
//	private void parse(Field field, Object obj, byte[] value) throws IllegalArgumentException, IllegalAccessException {
//		if (null == value || null == field)
//			return;
//		if (field.getType().equals(int.class)) {
//			integerSerializer.fromBytes(value);
//			field.setInt(obj, integerSerializer.fromBytes(value));
//		}else if(field.getType().equals(Integer.class)){
//			field.set(obj, integerSerializer.fromBytes(value));
//		} else if (field.getType().equals(long.class)) {
//			field.setLong(obj, longSerializer.fromBytes(value));
//		} else if (field.getType().equals(Long.class)) {
//			field.set(obj, longSerializer.fromBytes(value));
//		}  else if (field.getType().equals(String.class)) {
//			field.set(obj, stringSerializer.fromBytes(value));
//		} else {
//			throw new IllegalArgumentException("no match type:" + field.getType());
//		}
//	}
//
//	/**
//	 *
//	 * @param type
//	 * @param value
//	 * @return
//	 * @throws Exception
//	 */
//	private Object newObject(Type type, byte[] value) throws Exception {
//		if (type.equals(Integer.class) || type.equals(int.class)) {
//			return integerSerializer.fromBytes(value);
//		} else if (type.equals(Long.class) || type.equals(long.class)) {
//			return longSerializer.fromBytes(value);
//		} else if (type.equals(String.class)) {
//			return stringSerializer.fromBytes(value);
//		} else {
//			throw new IllegalArgumentException("no match type:" + type);
//		}
//	}
//
//	/**
//	 * a class collect entity's mapping with cassandra annotation
//	 */
//	private class CassFieldCollect {
//		private String ColumnFamilyName;
//		private Field rowkey;
//		private Map<String, Field> fieldsMap;
//		// key is "prefixName + delimiter",value is field map with Object
//		private Map<String, Field> dynamicFieldsMap;
//		// key is "prefixName + delimiter",value is type eg.
//		// List<CassDynamicType<String>> is "String Type"
//		private Map<String, Type> dynamicGenericMap;
//
//		private CassFieldCollect() {}
//
//		public CassFieldCollect(String columnFamilyName, Field rowkey, Map<String, Field> fieldsMap,
//				Map<String, Field> dynamicFieldsMap, Map<String, Type> dynamicGenericMap) {
//			super();
//			ColumnFamilyName = columnFamilyName;
//			this.rowkey = rowkey;
//			this.fieldsMap = fieldsMap;
//			this.dynamicFieldsMap = dynamicFieldsMap;
//			this.dynamicGenericMap = dynamicGenericMap;
//		}
//
//		public String getColumnFamilyName() {
//			return ColumnFamilyName;
//		}
//
//		public void setColumnFamilyName(String columnFamilyName) {
//			ColumnFamilyName = columnFamilyName;
//		}
//
//		public Field getRowkey() {
//			return rowkey;
//		}
//
//		public void setRowkey(Field rowkey) {
//			this.rowkey = rowkey;
//		}
//
//		public Map<String, Field> getFieldsMap() {
//			return fieldsMap;
//		}
//
//		public void setFieldsMap(Map<String, Field> fieldsMap) {
//			this.fieldsMap = fieldsMap;
//		}
//
//		public Map<String, Field> getDynamicFieldsMap() {
//			return dynamicFieldsMap;
//		}
//
//		public void setDynamicFieldsMap(Map<String, Field> dynamicFieldsMap) {
//			this.dynamicFieldsMap = dynamicFieldsMap;
//		}
//
//		public Map<String, Type> getDynamicGenericMap() {
//			return dynamicGenericMap;
//		}
//
//		public void setDynamicGenericMap(Map<String, Type> dynamicGenericMap) {
//			this.dynamicGenericMap = dynamicGenericMap;
//		};
//	}
//
//}