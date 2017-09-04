package com.advdsp.service.receive.business;

import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.processor.kafka.KafkaProcessor;
import com.advdsp.service.receive.ServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RequestBusiness extends BaseBusiness {

    private String receive_topic = ServiceConfiguration.getInstance().getConfig().getString("dsp.action.data.topic");
    private static Logger logger = LoggerFactory.getLogger(RequestBusiness.class.getName());

    public void clickSyncProcess(ActionDataModel actionDataModel) throws Throwable {
        KafkaProcessor.getKafkaProcessorInstance().processAdvClickData(actionDataModel, receive_topic);
    }

    public void callBackProcess(ActionDataModel actionDataModel) throws Throwable {
        KafkaProcessor.getKafkaProcessorInstance().processAdvActivationData(actionDataModel, receive_topic);
    }
}
