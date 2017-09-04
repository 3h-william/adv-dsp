package com.advdsp.service.receive.business;

import com.advdsp.service.dsp.processor.kafka.KafkaProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class BusinessFactory {

    private static Logger logger = LoggerFactory.getLogger(BusinessFactory.class.getName());

    private static RequestBusiness requestBusiness = new RequestBusiness();

    public static RequestBusiness getRequestBusiness() {
        return requestBusiness;
    }

    public static KafkaProcessor kafkaProcessor;

}
