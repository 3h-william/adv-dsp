package com.advdsp.service.dsp.common.kafka.consumer;

import com.advdsp.service.dsp.common.PropertyLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by william on 2017/7/6.
 */
public class KafkaMultiThread {
    protected static Logger logger = LoggerFactory.getLogger(KafkaMultiThread.class.getName());

    /**
     * kafka consumer的配置文件，在同一个jvm中，可以存在多个 consumer groups
     * 默认配置文件名为:  kafka-consumer.properties
     */
    private String kafka_consumer_properties_file = "kafka-consumer.properties";
    /**
     * 根据 kafak consumer 配置文件初始化参数
     */
    private Properties consumerProperties = null;
    /**
     * consumer的数量
     */
    private final int consumer_nums;

    private ExecutorService executor;

    private List<KafkaCommonConsumer> consumerList;

    private final String kafkaCommonConsumerClass;

    /**
     * 需要传入的 topics ，用逗号分隔
     **/
    private String topics;

    public KafkaMultiThread(String kafkaCommonConsumerClass, String topics) {
        this(kafkaCommonConsumerClass, null, topics);
    }

    public KafkaMultiThread(String kafkaCommonConsumerClass, String kafka_consumer_properties_file, String topics) {
        this.kafkaCommonConsumerClass = kafkaCommonConsumerClass;
        if (StringUtils.isNoneEmpty(kafka_consumer_properties_file)) {
            this.kafka_consumer_properties_file = kafka_consumer_properties_file;
        }
        this.consumerProperties = PropertyLoader.getPropertiesFromClasspath(this.kafka_consumer_properties_file);
        consumer_nums = Integer.parseInt(consumerProperties.getProperty("consumer.nums", "1"));
        this.topics = topics;
    }


    public void start() {
        logger.info("[KafkaMultiThread] start , consumer_nums = " + consumer_nums + " class = " + kafkaCommonConsumerClass);
        executor = Executors.newFixedThreadPool(consumer_nums);
        consumerList = new ArrayList<>(consumer_nums);

        for (int i = 0; i < consumer_nums; i++) {
            final KafkaCommonConsumer kafkaCommonConsumer;
            try {
                kafkaCommonConsumer = (KafkaCommonConsumer) Class.forName(kafkaCommonConsumerClass).newInstance();
                kafkaCommonConsumer.setConsumerName(String.valueOf(i));
                kafkaCommonConsumer.setTopics(topics);
                consumerList.add(kafkaCommonConsumer);
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            kafkaCommonConsumer.start();
                        } catch (Throwable t) {
                            logger.error("[KafkaMultiThread] start error", t);
                        }
                    }
                });
            } catch (Throwable t) {
                logger.error("[KafkaMultiThread] start error", t);
            }

        }
    }

    public void stop() {
        if (null != consumerList) {
            logger.info("[KafkaMultiThread] close kafka consumer");
            for (KafkaCommonConsumer kafkaCommonConsumer : consumerList) {
                kafkaCommonConsumer.close();
            }
            consumerList.clear();
        }

        if (null != executor) {
            logger.info("[KafkaMultiThread] shutdown thread pool");
            executor.shutdown();
        }
        logger.info("[KafkaMultiThread] stop over");
    }

}
