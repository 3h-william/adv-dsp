package com.advdsp.service.dsp.processor.kafka;

import com.advdsp.service.dsp.common.PropertyLoader;
import com.advdsp.service.dsp.common.check.DataCommonCheck;
import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.common.serialize.DataSerialize;
import com.advdsp.service.dsp.common.type.DataTypeUtils;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static com.advdsp.service.dsp.common.type.KeyUtils.generateKeyWithTs;

/**
 *
 */
public class KafkaProcessor {

    private static KafkaProcessor processor = new KafkaProcessor();

    public static KafkaProcessor getKafkaProcessorInstance() {
        return processor;
    }

    private Properties kafkaProperties = PropertyLoader.getPropertiesFromClasspath("kafka-producer.properties");
    private Producer<String, String> producer = new KafkaProducer<>(kafkaProperties);
    //private String dsp_action_data_topic = requireNonNull(kafkaProperties.getProperty("dsp.action.data.topic"), "dsp.action.data.topic is empty");

    /**
     * 存储广告点击数据 , from 渠道方 ,type = ClickSync
     */
    public void processAdvClickData(ActionDataModel actionDataModel, String topic) throws AdvDspException, JsonProcessingException {
        DataCommonCheck.checkActionData(actionDataModel);
        if (!DataTypeUtils.isClickSyncType(actionDataModel.getType())) {
            throw new AdvDspException("type is error , type = " + actionDataModel.getType());
        }
        sendData(topic, generateKeyWithTs(actionDataModel), DataSerialize.serializeActivationData(actionDataModel));
    }


    /**
     * 存储广告激活数据 , from 广告主  ,type = CallBack
     */
    public void processAdvActivationData(ActionDataModel actionDataModel, String topic) throws AdvDspException, JsonProcessingException {
        DataCommonCheck.checkActionData(actionDataModel);
        if (!DataTypeUtils.isCallBackType(actionDataModel.getType())) {
            throw new AdvDspException("type is error , type = " + actionDataModel.getType());
        }
        sendData(topic, generateKeyWithTs(actionDataModel), DataSerialize.serializeActivationData(actionDataModel));
    }

    /**
     * errorActionDataModel  message log
     */
    public void processErrorActivationData(ActionDataModel actionDataModel, String topic) throws AdvDspException, JsonProcessingException {
        sendData(topic, generateKeyWithTs(actionDataModel), DataSerialize.serializeActivationData(actionDataModel));
    }


    private void sendData(String topic, Object key, Object value) {
        producer.send(new ProducerRecord(topic, key, value));
    }

}
