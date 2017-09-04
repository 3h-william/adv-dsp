package com.advdsp.service.send.consumer;

import com.advdsp.service.dsp.common.check.DataCommonCheck;
import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.common.serialize.DataSerialize;
import com.advdsp.service.dsp.common.type.ActionDataType;
import com.advdsp.service.dsp.common.type.DataTypeUtils;
import com.advdsp.service.dsp.common.type.KeyUtils;
import com.advdsp.service.dsp.common.kafka.consumer.KafkaNewConsumer;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.processor.kafka.KafkaProcessor;
import com.advdsp.service.dsp.processor.kv.KVDataProcessor;
import com.advdsp.service.dsp.processor.kv.KVProcessorFactory;
import com.advdsp.service.send.ServiceConfiguration;
import com.advdsp.statistic.common.StatisticProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import static java.util.Objects.*;

/**
 */
public class DspDataConsumer extends KafkaNewConsumer<String, String> {

    private KVDataProcessor kvDataProcessor = KVProcessorFactory.getInstance();
    private DspDataTransfer dspDataTransfer = new DspDataTransfer();
    private String error_topic = ServiceConfiguration.getInstance().getConfig().getString("dsp.data.error.topic");

    @Override
    public void processorMessage(String topic, String key, String value) throws Throwable {
        String keyType = KeyUtils.getKeyType(key);
        // 如果是点击数据
        if (DataTypeUtils.isClickSyncType(keyType)) {
            ActionDataModel actionDataModel = requireNonNull(DataSerialize.deserializeActivationData(value), "actionDataModel is empty ");
            DataCommonCheck.checkActionData(actionDataModel);
            // 1st . 第一次出现 ，用来匹配广告归因
            if (!kvDataProcessor.isActionDataExist(actionDataModel)) {
                kvDataProcessor.saveActionDataWithTTL(actionDataModel);

            }
            // 用以统计每日排重点击数
            if(!kvDataProcessor.isClickActionDataWithDs(actionDataModel)){
                kvDataProcessor.saveClickActionDataWithDs(actionDataModel);
                StatisticProcessor.getInstance().incDistinctClickNums(actionDataModel);
            }
            StatisticProcessor.getInstance().incClickNums(actionDataModel);
            // 2nd . 转发给广告主
            try {
                dspDataTransfer.clickDataTransfer(actionDataModel);
            } catch (Throwable t) {
                KafkaProcessor.getKafkaProcessorInstance().processErrorActivationData(actionDataModel, error_topic);
                logger.error(getDescribeName() + "dspDataTransfer failed , actionDataModelCopy = " + actionDataModel.toString() + "," + ExceptionUtils.getRootCauseMessage(t));
                //throw new AdvDspException("dspDataTransfer failed", t);
            }
        }
        // 如果是回调数据 (激活回调)
        else if (DataTypeUtils.isCallBackType(keyType)) {
            ActionDataModel actionDataModel = requireNonNull(DataSerialize.deserializeActivationData(value), "actionDataModel is empty ");
            DataCommonCheck.checkActionData(actionDataModel);
            // 统计数据
            StatisticProcessor.getInstance().incActivationNums(actionDataModel);
            // 重复激活
            if (kvDataProcessor.isActionDataExist(actionDataModel)) {
                // 重复激活
            }
            // 首次激活
            else {
                // 保存激活数据
                kvDataProcessor.saveActionData(actionDataModel);
                StatisticProcessor.getInstance().incDistinctActivationNums(actionDataModel);
                // 2nd . 转发给渠道方
                // 构建点击基本信息，只为了从kv中查询
                ActionDataModel temp_click_actionDataModel = new ActionDataModel();
                temp_click_actionDataModel.setType(ActionDataType.ClickSync.getValue());
                temp_click_actionDataModel.setIdfa(actionDataModel.getIdfa());
                temp_click_actionDataModel.setIp(actionDataModel.getIp());
                temp_click_actionDataModel.setAppid(actionDataModel.getAppid());
                String rowkey = KeyUtils.generateKeyWithNoTs(temp_click_actionDataModel);
                ActionDataModel match_actionDataModel = kvDataProcessor.getActionData(rowkey);
                // 非自然激活
                if (null != match_actionDataModel) {
                    //match_actionDataModel.setRowkey(KeyUtils.generateKeyWithTs(actionDataModel));
                    //kvDataProcessor.saveActionData(match_actionDataModel);
                    logger.info(getDescribeName() + "click data match , rowkey = " + rowkey);
                    // 匹配到转发 transfer
                    try {
                        dspDataTransfer.activationDataTransfer(match_actionDataModel);
                    } catch (Throwable t) {
                        // 错误统计
                        KafkaProcessor.getKafkaProcessorInstance().processErrorActivationData(actionDataModel, error_topic);
                        throw t;
                    }
                } else {
                    // 自然激活
                    logger.info(getDescribeName() + "activation data not match ,natural active , rowkey = " + rowkey);
                }
            }
        } else {
            throw new AdvDspException(getDescribeName() + "data type is error ,type = " + keyType);
        }
    }


    @Override
    public void finalProcessorMessage(String topic, String key, String value) {
        commitOffset();
    }

    @Override
    public void preProcessorMessage(String topic, String key, String value) {
        logger.info(getDescribeName() + "fetch data , key = " + key);
    }
}
