package com.advdsp.service.dsp.common.kafka.consumer;

import com.advdsp.service.dsp.common.PropertyLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

/**
 */
public abstract class KafkaNewConsumer<K, V> extends KafkaCommonConsumer<K, V> {

    private Properties consumerProperties = PropertyLoader.getPropertiesFromClasspath("kafka-consumer.properties");
    private long pool_timeout = Long.parseLong(consumerProperties.getProperty("consumer.pool.timeout", "100"));
    private KafkaConsumer<K, V> consumer;
    private AtomicBoolean isStarted = new AtomicBoolean(false);
    private AtomicBoolean isClosed = new AtomicBoolean(false);
    protected long messageConsumerCount = 0;

    @Override
    public void start() {
        consumerProperties.setProperty("client.id", getConsumerName());
        consumer = new KafkaConsumer<>(consumerProperties);
        if (isStarted.get()) {
            throw new RuntimeException("consumer has been started");
        }
        isClosed.set(false);
        isStarted.set(true);
        // start backend thread
        new Thread(new MetricsCollectBackThread()).start();

        if (StringUtils.isNoneEmpty(topics)) {
            subscribe(topics);
        } else {
            logger.warn(getDescribeName() + " topics is empty , not subscribe now");
        }

        // main entry
        while (!isClosed.get()) {
            try {
                ConsumerRecords<K, V> records = consumer.poll(pool_timeout);
                for (ConsumerRecord<K, V> record : records) {
                    try {
                        preProcessorMessage(record.topic(), record.key(), record.value());
                        processorMessage(record.topic(), record.key(), record.value());
                        postProcessorMessage(record.topic(), record.key(), record.value());
                    } catch (Throwable t) {
                        errorProcessorMessage(record.topic(), record.key(), record.value(), t);
                        logger.error(getDescribeName() + " processor message error ", t);
                    } finally {
                        finalProcessorMessage(record.topic(), record.key(), record.value());
                        messageConsumerCount++;
                    }
                }
            } catch (Throwable t) {
                logger.error(getDescribeName() + " consumer pool message error ", t);
            }

        }
    }

    @Override
    public void commitOffset() {
        consumer.commitSync();
    }

    public class MetricsCollectBackThread implements Runnable {
        @Override
        public void run() {
            logger.info(getDescribeName() + " consumer monitor start");
            while (!isClosed.get()) {
                try {
                    logger.info(getDescribeName() + " consumer message count:" + messageConsumerCount);
                } catch (Throwable t) {
                    logger.warn(getDescribeName() + " run error ", t);
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    logger.warn(getDescribeName() + " sleep error ", e);
                } finally {

                }
            }
            logger.info(getDescribeName() + " consumer monitor exit");
        }
    }

    @Override
    public void processorMessage(String topic, K key, V value) throws Throwable {
        // customer implement
    }

    @Override
    public void preProcessorMessage(String topic, K key, V value) {
        // customer implement
    }

    @Override
    public void postProcessorMessage(String topic, K key, V value) {
        // customer implement
    }

    @Override
    public void errorProcessorMessage(String topic, K key, V value, Throwable t) {
        // customer implement
    }

    @Override
    public void finalProcessorMessage(String topic, K key, V value) {
        // customer implement
    }

    @Override
    public void subscribe(String topics) {
        // 如果consumer已经关闭，则不订阅
        if (!isClosed.get()) {
            consumer.subscribe(Arrays.asList(requireNonNull(topics, "topics is null").split(",")));
        } else {
            throw new RuntimeException("consume is closed , subscribe dose not allow");
        }
    }

    @Override
    public void close() {

        if (isClosed.get()) {
            throw new RuntimeException("consumer has been closed");
        } else {
            isClosed.set(true);
            consumer.close();
        }
    }
}