package com.advdsp.service.dsp.common.kafka.consumer;

/**
 * Created by william on 2017/7/6.
 */
public class KafkaCommonConsumerTest {

    public static void main(String[] args) {

//        KafkaCommonConsumer<String ,String > kafkaCommonConsumer = new MyConsumer();
//
//        kafkaCommonConsumer.start();

        KafkaMultiThread kafkaMultiThread = new KafkaMultiThread("com.advdsp.service.dsp.common.kafka.consumer.MyConsumer","dsp-data");
        kafkaMultiThread.start();

    }

}
