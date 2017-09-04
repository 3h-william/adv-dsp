package com.advdsp.service.manager.db.dto;

import java.util.Date;

/**
 * 广告商配置
 */
public class CustomerConfigDto {

    private Integer customer_id;
    private String customer_name;
    private String customer_describe;
    private Date last_edit_date;
    private String last_edit_user_id;
    private String last_edit_user_name;

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_describe() {
        return customer_describe;
    }

    public void setCustomer_describe(String customer_describe) {
        this.customer_describe = customer_describe;
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
