package com.advdsp.service.send.consumer;

import com.advdsp.service.dsp.common.check.DataCommonCheck;
import com.advdsp.service.dsp.common.kafka.consumer.KafkaNewConsumer;
import com.advdsp.service.dsp.common.serialize.DataSerialize;
import com.advdsp.service.dsp.common.type.DataTypeUtils;
import com.advdsp.service.dsp.common.type.KeyUtils;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.processor.kv.KVDataProcessor;
import com.advdsp.service.dsp.processor.kv.KVProcessorFactory;
import com.advdsp.service.send.db.dto.AdxConfigDto;
import com.advdsp.service.send.memory.AdxConfigInfo;
import com.advdsp.statistic.common.StatisticProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 */
public class DspCopyDataConsumer extends KafkaNewConsumer<String, String> {

    private KVDataProcessor kvDataProcessor = KVProcessorFactory.getInstance();
    private DspDataTransfer dspDataTransfer = new DspDataTransfer();

    /**
     * 自由渠道，默认0
     * TODO
     */
    private final Integer selfChannel = 0;

    @Override
    public void processorMessage(String topic, String key, String value) throws Throwable {
        String keyType = KeyUtils.getKeyType(key);
        // 如果是点击数据
        if (DataTypeUtils.isClickSyncType(keyType)) {
            ActionDataModel actionDataModel = requireNonNull(DataSerialize.deserializeActivationData(value), "actionDataModel is empty ");
            DataCommonCheck.checkActionData(actionDataModel);
            List<AdxConfigDto> adxConfigDtoList = AdxConfigInfo.getAdxConfigInfoInstance().getApplyAdxConfigList(Integer.parseInt(actionDataModel.getChannel()), Integer.parseInt(actionDataModel.getAppid()));
            if(null!=adxConfigDtoList){
                // 循环构建 需要流量copy的 点击数据，进行转发
                for (AdxConfigDto adxConfigDto : adxConfigDtoList) {
                    if (probabilityTransfer(adxConfigDto.getAmount())) {
                        ActionDataModel actionDataModelCopy = new ActionDataModel();
                        actionDataModelCopy.setType(actionDataModel.getType());
                        actionDataModelCopy.setIdfa(actionDataModel.getIdfa());
                        actionDataModelCopy.setIp(actionDataModel.getIp());
                        //actionDataModelCopy.setCallback(callback);
                        actionDataModelCopy.setChannel(String.valueOf(selfChannel)); // 自有渠道
                        actionDataModelCopy.setAppid(String.valueOf(adxConfigDto.getApply_app_id())); // set applyID
                        actionDataModelCopy.setTimestamp(String.valueOf(System.currentTimeMillis()));
                        try {
                            //先转发
                            dspDataTransfer.clickDataTransfer(actionDataModelCopy);
                            //再做统计
                            StatisticProcessor.getInstance().incClickNums(actionDataModelCopy);
                            // 第一次出现 ，用来匹配广告归因
                            if (!kvDataProcessor.isActionDataExist(actionDataModelCopy)) {
                                kvDataProcessor.saveActionDataWithTTL(actionDataModelCopy);
                            }
                            // 用以统计每日排重点击数
                            if(!kvDataProcessor.isClickActionDataWithDs(actionDataModelCopy)){
                                kvDataProcessor.saveClickActionDataWithDs(actionDataModelCopy);
                                StatisticProcessor.getInstance().incDistinctClickNums(actionDataModelCopy);
                            }
                            logger.info(getDescribeName() + "dspDataTransfer copy , key =" + KeyUtils.generateKeyWithTs(actionDataModelCopy));
                        } catch (Throwable t) {
                            logger.error(getDescribeName() + "dspDataTransfer copy failed , actionDataModelCopy = " + actionDataModelCopy.toString() + "," + ExceptionUtils.getRootCauseMessage(t));
                        }
                    } else {
                        // skip
                    }
                }
            }
        }
    }

    /**
     * 根据扣量比，概率决定，是否转发
     *
     * @param factor
     * @return
     */
    private boolean probabilityTransfer(Integer factor) {
        if (factor >= 100) {
            return true;
        } else if (factor <= 0) {
            return false;
        } else return Math.random() * 100 < factor;

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
