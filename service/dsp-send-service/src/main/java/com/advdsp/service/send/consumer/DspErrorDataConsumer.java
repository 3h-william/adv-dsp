package com.advdsp.service.send.consumer;

import com.advdsp.service.dsp.common.check.DataCommonCheck;
import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.common.kafka.consumer.KafkaNewConsumer;
import com.advdsp.service.dsp.common.serialize.DataSerialize;
import com.advdsp.service.dsp.common.type.ActionDataType;
import com.advdsp.service.dsp.common.type.DataTypeUtils;
import com.advdsp.service.dsp.common.type.KeyUtils;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.processor.kafka.KafkaProcessor;
import com.advdsp.service.dsp.processor.kv.KVDataProcessor;
import com.advdsp.service.dsp.processor.kv.KVProcessorFactory;
import com.advdsp.service.send.ServiceConfiguration;
import com.advdsp.service.send.db.dto.SystemConfigDto;
import com.advdsp.statistic.common.StatisticProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import static java.util.Objects.requireNonNull;

/**
 */
public class DspErrorDataConsumer extends KafkaNewConsumer<String, String> {

    private DspDataTransfer dspDataTransfer = new DspDataTransfer();
    private String error_topic = ServiceConfiguration.getInstance().getConfig().getString("dsp.data.error.topic");
    // 重试保留一天，超过一天，直接废弃
    private final long Max_ERROR_RETRY_TIME = 24 * 60 * 60 * 1000L;

    @Override
    public void processorMessage(String topic, String key, String value) throws Throwable {
        String keyType = KeyUtils.getKeyType(key);
        // 如果是点击数据
        if (DataTypeUtils.isClickSyncType(keyType)) {
            ActionDataModel actionDataModel = requireNonNull(DataSerialize.deserializeActivationData(value), "actionDataModel is empty ");
            DataCommonCheck.checkActionData(actionDataModel);
            try {
                dspDataTransfer.clickDataTransfer(actionDataModel);
            } catch (TransferRequestException te) {
                if ((System.currentTimeMillis() - Long.parseLong(actionDataModel.getTimestamp())) > Max_ERROR_RETRY_TIME) {
                    //废弃记录
                    logger.error(getDescribeName() + " retry time is over than 1 day, do not process again , errordata = " + actionDataModel.toString() + "," + ExceptionUtils.getRootCauseMessage(te));
                }else{
                    KafkaProcessor.getKafkaProcessorInstance().processErrorActivationData(actionDataModel, error_topic);
                    logger.error(getDescribeName() + " failed retry again , errordata = " + actionDataModel.toString() + "," + ExceptionUtils.getRootCauseMessage(te));
                }
            } catch (Throwable t) {
                throw new AdvDspException("dspDataTransfer failed", t);
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
