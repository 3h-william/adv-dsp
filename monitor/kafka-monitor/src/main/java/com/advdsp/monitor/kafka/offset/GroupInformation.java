package com.advdsp.monitor.kafka.offset;

/**
 */
public class GroupInformation {

    private String group_name;
    private String topic_name;
    private Integer partition_num;
    private Long current_offset;
    private Long log_end_offset;
    private Long lag;
    private String owner;


    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public Integer getPartition_num() {
        return partition_num;
    }

    public void setPartition_num(Integer partition_num) {
        this.partition_num = partition_num;
    }

    public Long getCurrent_offset() {
        return current_offset;
    }

    public void setCurrent_offset(Long current_offset) {
        this.current_offset = current_offset;
    }

    public Long getLog_end_offset() {
        return log_end_offset;
    }

    public void setLog_end_offset(Long log_end_offset) {
        this.log_end_offset = log_end_offset;
    }

    public Long getLag() {
        return lag;
    }

    public void setLag(Long lag) {
        this.lag = lag;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
