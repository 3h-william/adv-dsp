package com.advdsp.service.send.db.dto;

/**
 */
public class AppBaseConfigDto {
    private String app_id;
    private String customer_id;
    private String callback_url_base;
    private Integer system_id;
    private String app_name;
    private Long last_edit_date;
    private String last_edit_user_id;


    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCallback_url_base() {
        return callback_url_base;
    }

    public void setCallback_url_base(String callback_url_base) {
        this.callback_url_base = callback_url_base;
    }

    public Integer getSystem_id() {
        return system_id;
    }

    public void setSystem_id(Integer system_id) {
        this.system_id = system_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public Long getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Long last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public String getLast_edit_user_id() {
        return last_edit_user_id;
    }

    public void setLast_edit_user_id(String last_edit_user_id) {
        this.last_edit_user_id = last_edit_user_id;
    }
}
