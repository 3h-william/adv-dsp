package com.advdsp.monitor.kafka.offset;

/**
 */
public class GroupInformationStatistic {
    private String topic_name;
    private String group_name;
    private int partition_nums;

    private long lag_max;
    private long lag_mean;
    private long lag_total;

    private boolean miss;

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getPartition_nums() {
        return partition_nums;
    }

    public void setPartition_nums(int partition_nums) {
        this.partition_nums = partition_nums;
    }

    public long getLag_max() {
        return lag_max;
    }

    public void setLag_max(long lag_max) {
        this.lag_max = lag_max;
    }

    public long getLag_mean() {
        return lag_mean;
    }

    public void setLag_mean(long lag_mean) {
        this.lag_mean = lag_mean;
    }

    public long getLag_total() {
        return lag_total;
    }

    public void setLag_total(long lag_total) {
        this.lag_total = lag_total;
    }

    public boolean isMiss() {
        return miss;
    }

    public void setMiss(boolean miss) {
        this.miss = miss;
    }
}
