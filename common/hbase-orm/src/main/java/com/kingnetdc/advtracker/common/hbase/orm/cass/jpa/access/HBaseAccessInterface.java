package com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access;

import java.util.List;


/**
 * @author wz68
 */
public interface HBaseAccessInterface {

    /**
     * save a dto
     *
     * @param value
     * @throws Exception
     */
    void save(Object value) throws Exception;


//    /**
//     * commit all buffer objects
//     *
//     * @throws Exception
//     */
//    public void flushCommits() throws Exception;


    /**
     * get object with rowkey
     *
     * @param rowkey
     * @param clazz
     * @return
     * @throws Exception
     */
    public <T> T getByRowkey(Object rowkey, Class<T> clazz) throws Exception;


    /**
     * get object(List) with rowkeys
     *
     * @param rowkeys
     * @param clazz
     * @return
     * @throws Exception
     */
    <T> List<T> getByRowkeys(List<Object> rowkeys, Class<T> clazz) throws Exception;


    /**
     * update specify columns
     * <p>
     * if dynamic column,the specifyColumnName=preFixName+delimiter
     *
     * @param value
     * @param specifyColumnNames
     * @throws Exception
     */
    void updateColumns(Object value, List<String> specifyColumnNames) throws Exception;

    /**
     * update specify columns
     * <p>
     * if dynamic column,the specifyColumnName=preFixName+delimiter
     *
     * @param value
     * @param specifyColumnNames
     * @throws Exception
     */
    void updateColumns(Object value, List<String> specifyColumnNames, Long timeStamp) throws Exception;


    /**
     * update specify column
     * <p>
     * if dynamic column,the specifyColumnName=preFixName+delimiter
     *
     * @param value
     * @param specifyColumnName
     * @throws Exception
     */
    public void updateColumn(Object value, String specifyColumnName) throws Exception;


    /**
     * update specify column
     * <p>
     * if dynamic column,the specifyColumnName=preFixName+delimiter
     *
     * @param value
     * @param specifyColumnName
     * @throws Exception
     */
    public void updateColumn(Object value, String specifyColumnName, Long timeStamp) throws Exception;


    /**
     * delete by rowkey
     *
     * @param <T>
     * @param rowkey
     * @param clazz
     * @throws Exception
     */
    <T> void deleteByRowkey(Object rowkey, Class<T> clazz) throws Exception;

    /**
     * delete specifyColumn with names in one row
     *
     * @param <T>
     * @param rowkey
     * @param clazz
     * @param specifyColumnNames
     * @throws Exception
     */
    <T> void deleteByRowkey(Object rowkey, Class<T> clazz, List<String> specifyColumnNames) throws Exception;

    /**
     * is rowkey exist
     * @param rowkey
     * @param clazz
     * @param <T>
     * @return   true exist / false miss
     * @throws Exception
     */
    <T> boolean isKeyExist(Object rowkey, Class<T> clazz) throws Exception;

}
