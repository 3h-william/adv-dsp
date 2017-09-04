package com.advdsp.service.dsp.processor.kv;

import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.model.CounterDataModel;

import java.io.IOException;
import java.util.List;

/**
 */
public interface KVDataProcessor {

    /**
     * todo 性能优化
     */
    boolean isActionDataExist(ActionDataModel actionDataModel) throws Throwable;

    void saveActionData(ActionDataModel actionDataModel) throws Throwable;

    void saveActionDataWithTTL(ActionDataModel actionDataModel) throws Throwable;

    boolean isActionDataWithTsExist(ActionDataModel actionDataModel) throws Throwable;

    void saveActionDataWithTs(ActionDataModel actionDataModel) throws Throwable;

    boolean isClickActionDataWithDs(ActionDataModel actionDataModel) throws Throwable;

    void saveClickActionDataWithDs(ActionDataModel actionDataModel) throws Throwable;

    ActionDataModel getActionData(String key) throws Throwable;

    ActionDataModel getActionDataWithTs(String key) throws Throwable;

    void addCounter(String counterKey, String indicatorValue) throws Throwable;

    /**
     * 获取一个时间段内所有的计数器
     * @param startTime
     * @param endTime
     * @return
     * @throws Throwable
     */
    List<CounterDataModel> getAllCounters(Long startTime , Long endTime) throws Throwable;


    /**
     * 修数据操作
     * @return
     * @throws Throwable
     */
    void mergeCounters(String rowkey) throws Throwable;
}
