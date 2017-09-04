package com.advdsp.service.manager.db.dto;

import java.util.Date;

/**
 * 系统配置
 */
public class SystemConfigDto {

    private Integer system_id;
    private String system_name;
    private String callback_url_base;
    private Date last_edit_date;
    private String last_edit_user_id;
    private String last_edit_user_name;

    public Integer getSystem_id() {
        return system_id;
    }

    public void setSystem_id(Integer system_id) {
        this.system_id = system_id;
    }

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public String getCallback_url_base() {
        return callback_url_base;
    }

    public void setCallback_url_base(String callback_url_base) {
        this.callback_url_base = callback_url_base;
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
