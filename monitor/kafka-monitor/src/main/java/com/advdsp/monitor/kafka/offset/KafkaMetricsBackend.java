package com.advdsp.monitor.kafka.offset;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * TODO 优化consumer的链接开销
 */
public class KafkaMetricsBackend implements Runnable {
    protected static Logger logger = LoggerFactory.getLogger(KafkaMetricsBackend.class.getName());
    private FastDateFormat dsDateFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZ");
    private Map<String, ConsumerGroupMonitor> consumerGroupMonitorMap = new HashMap<>();

    /**
     * 格式  ${group}:${topic}
     */
    private final String[] monitor_group_topics;
    /**
     * 单位秒
     */
    private final int kafka_metrics_interval;

    private String bootstrapServers;

    public KafkaMetricsBackend(String[] monitor_group_topics, Integer kafka_metrics_interval, String bootstrapServers) {
        this.monitor_group_topics = monitor_group_topics;
        this.kafka_metrics_interval = kafka_metrics_interval;
        this.bootstrapServers = bootstrapServers;
    }

    @Override
    public void run() {
        while (true) {
            logger.info("[KafkaMetricsBackend] start , monitor_group_topics_size=" + monitor_group_topics.length + ", interval=" + kafka_metrics_interval
                    + ",bootstrapServers=" + bootstrapServers
            );
            for (String group_topic : monitor_group_topics) {
                try {
                    logger.warn("[KafkaMetricsBackend] start to collect group_topic=" + group_topic);
                    String splits[] = group_topic.split(":");
                    String group = splits[0];
                    String topic = splits[1];
                    ConsumerGroupMonitor consumerGroupMonitor = consumerGroupMonitorMap.get(group_topic);
                    // get from cache
                    if (null == consumerGroupMonitor) {
                        consumerGroupMonitor = new ConsumerGroupMonitor(group, bootstrapServers);
                        consumerGroupMonitorMap.put(group_topic, consumerGroupMonitor);
                    }
                    consumerGroupMonitor.init();
                    consumerGroupMonitor.describeGroup(group);
                    GroupInformationCollect groupInformationCollect = consumerGroupMonitor.groupInformationCollect();
                    GroupInformationStatistic groupInformationStatistic = groupInformationCollect.getStatistic(topic);
                    if (null != groupInformationStatistic) {
                        //data-normal-lag-max
                        execCommand(generateCmdStr(group, topic, "lag-max", String.valueOf(groupInformationStatistic.getLag_max())));
                        //data-normal-lag-mean
                        execCommand(generateCmdStr(group, topic, "lag-mean", String.valueOf(groupInformationStatistic.getLag_mean())));
                        //data-normal-lag-total
                        execCommand(generateCmdStr(group, topic, "lag-total", String.valueOf(groupInformationStatistic.getLag_total())));
                    } else {
                        logger.warn("[KafkaMetricsBackend] collect miss , group_topic = " + group_topic);
                    }
                } catch (Throwable t) {
                    logger.error("[KafkaMetricsBackend] group_topic=" + group_topic + " failed", t);
                }
            }

            try {
                Thread.sleep(kafka_metrics_interval * 1000L);
            } catch (InterruptedException e) {
                logger.error("[KafkaMetricsBackend] sleep failed ", e);
            }
        }

    }


    /**
     * aws cloudwatch put-metric-data --metric-name data-normal:dsp-datalag-total --namespace kafka --dimensions  topic=dsp-data  group=data-normal --timestamp 2017-07-21T18:23:36+0
     * 800 --value 10 --unit Milliseconds
     *
     * @return
     */
    public String generateCmdStr(String group, String topic, String metrics_name, String metrics_value) {
        List<String> cmdList = new ArrayList<>();
        cmdList.add("aws");
        cmdList.add("cloudwatch");
        cmdList.add("put-metric-data");
        cmdList.add("--metric-name");
        cmdList.add(metrics_name);
        cmdList.add("--namespace");
        cmdList.add("kafka");
        cmdList.add("--dimensions");
        cmdList.add("topic=" + topic + ",group=" + group);
        cmdList.add("--timestamp");
        cmdList.add(dsDateFormat.format(new Date()));
        cmdList.add("--value");
        cmdList.add(metrics_value);
        cmdList.add("--unit");
        cmdList.add("Count");
        return StringUtils.join(cmdList.toArray(), " ");
    }

    public void execCommand(String cmd) throws IOException, InterruptedException {
        logger.info("cmd=" + cmd);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor(10, TimeUnit.SECONDS);
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new KafkaMetricsBackend(new String[]{"data-normal:dsp-data"}, 5, "localhost:9092")).start();
    }
}
