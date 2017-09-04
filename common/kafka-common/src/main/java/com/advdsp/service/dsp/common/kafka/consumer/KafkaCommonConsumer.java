package com.advdsp.service.dsp.common.kafka.consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * wrap kafkaConsumer & low level(Simple) Consumer
 */
public abstract class KafkaCommonConsumer<K, V> {
    protected static Logger logger = LoggerFactory.getLogger(KafkaCommonConsumer.class.getName());
    protected String consumerName = "default";
    protected String topics;

//    public KafkaCommonConsumer(String consumerName, String topics) {
//        if (null != this.consumerName) {
//            this.consumerName = consumerName;
//        }
//        this.topicsList = Arrays.asList(requireNonNull(topics, "topics is null").split(","));
//        if (null == topicsList || topicsList.size() < 1) {
//            throw new IllegalArgumentException("topic is emptyt");
//        }
//        logger.info(getDescribeName() + "subscribe topic : " + topics);
//    }
//
//    public KafkaCommonConsumer(String topics) {
//        this(null, topics);
//    }


    public String getConsumerName() {
        return consumerName;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    protected String getDescribeName() {
        return "[KafkaConsumer-" + consumerName + "]";
    }

    public abstract void start();

    public abstract void processorMessage(String topic, K key, V value) throws Throwable;

    public abstract void preProcessorMessage(String topic, K key, V value);

    public abstract void postProcessorMessage(String topic, K key, V value);

    public abstract void finalProcessorMessage(String topic, K key, V value);

    public abstract void errorProcessorMessage(String topic, K key, V value, Throwable t);

    public abstract void commitOffset();

    public abstract void close();

    public abstract void subscribe(String topics);
}
