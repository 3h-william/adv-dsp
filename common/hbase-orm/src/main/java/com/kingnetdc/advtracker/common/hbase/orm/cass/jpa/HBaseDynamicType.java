package main.java.com.kingnetdc.advtracker.common.hbase.orm.cass.jpa;

/**
 * Cassandra Dynamic Type class
 * 
 * 
 * @author wz68
 *
 */
public class HBaseDynamicType<T> {
	/**
	 *  suffix column name
	 *  
	 *  can not be "" or null
	 */
	private String suffixColumnName;
	
	private T value;
	
	

	public String getSuffixColumnName() {
		return suffixColumnName;
	}

	public void setSuffixColumnName(String suffixColumnName) {
		this.suffixColumnName = suffixColumnName;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
