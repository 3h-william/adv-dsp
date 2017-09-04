package com.advdsp.service.manager.db.dto;

import java.util.Date;

/**
 * 渠道配置
 */
public class ChannelConfigDto {

    private Integer channel_id;
    private String channel_name;
    private String channel_describe;
    private Date last_edit_date;
    private String last_edit_user_id;
    private String last_edit_user_name;

    public Integer getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Integer channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_describe() {
        return channel_describe;
    }

    public void setChannel_describe(String channel_describe) {
        this.channel_describe = channel_describe;
    }

    public Date getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Date last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public String getLast_edit_user_id() {
        return last_edit_user_id;
    }

    public void setLast_edit_user_id(String last_edit_user_id) {
        this.last_edit_user_id = last_edit_user_id;
    }

    public String getLast_edit_user_name() {
        return last_edit_user_name;
    }

    public void setLast_edit_user_name(String last_edit_user_name) {
        this.last_edit_user_name = last_edit_user_name;
    }
}
