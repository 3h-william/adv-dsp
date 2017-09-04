package com.advdsp.statistic.common;

import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.processor.kv.KVProcessorFactory;

import java.text.SimpleDateFormat;

import static com.advdsp.service.dsp.common.type.IndicatorDataType.*;

/**
 */
public class StatisticProcessor {

    private static StatisticProcessor statisticProcessor = new StatisticProcessor();

    public static StatisticProcessor getInstance() {
        return statisticProcessor;
    }

    /**
     * 点击数
     *
     * @param actionDataModel
     */
    public void incClickNums(ActionDataModel actionDataModel) throws Throwable {
        KVProcessorFactory.getInstance().addCounter(generateCounterKey(actionDataModel), Click_Nums.getValue());
    }

    /**
     * 排重点击数
     *
     * @param actionDataModel
     */
    public void incDistinctClickNums(ActionDataModel actionDataModel) throws Throwable {
        KVProcessorFactory.getInstance().addCounter(generateCounterKey(actionDataModel), Distinct_Click_Nums.getValue());
    }

    /**
     * 接受激活数
     *
     * @param actionDataModel
     */
    public void incActivationNums(ActionDataModel actionDataModel) throws Throwable {
        KVProcessorFactory.getInstance().addCounter(generateCounterKey(actionDataModel), Activation_Nums.getValue());
    }

    /**
     * 排重激活数
     *
     * @param actionDataModel
     */
    public void incDistinctActivationNums(ActionDataModel actionDataModel) throws Throwable {
        KVProcessorFactory.getInstance().addCounter(generateCounterKey(actionDataModel), Distinct_Activation_Nums.getValue());
    }

    /**
     * 计数器的key
     * <p>
     * {ds}_{appID}_{channel_id}
     *
     * @param actionDataModel
     * @return
     */
    public String generateCounterKey(ActionDataModel actionDataModel) {
        SimpleDateFormat dsFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ds = dsFormat.format(Long.parseLong(actionDataModel.getTimestamp()));
        return ds + "_" + actionDataModel.getAppid() + "_" + actionDataModel.getChannel();
    }


}
