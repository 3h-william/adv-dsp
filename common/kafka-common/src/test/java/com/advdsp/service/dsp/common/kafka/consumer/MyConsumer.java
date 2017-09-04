package com.advdsp.service.dsp.common.kafka.consumer;

public class MyConsumer extends KafkaNewConsumer<String, String> {

    @Override
    public void processorMessage(String topic, String key, String value) {
        System.out.println("print key = " + key + ", value = " + value);
    }


}