package com.advdsp.service.send.db.dto;

/**
 */
public class AdxConfigDto {

    private Integer adx_id;
    private Integer channel_id;
    private Integer copy_app_id;
    private Integer apply_app_id;
    private Integer amount;
    private Long last_edit_date;
    private String last_edit_user_id;


    public Integer getAdx_id() {
        return adx_id;
    }

    public void setAdx_id(Integer adx_id) {
        this.adx_id = adx_id;
    }

    public Integer getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Integer channel_id) {
        this.channel_id = channel_id;
    }

    public Integer getCopy_app_id() {
        return copy_app_id;
    }

    public void setCopy_app_id(Integer copy_app_id) {
        this.copy_app_id = copy_app_id;
    }

    public Integer getApply_app_id() {
        return apply_app_id;
    }

    public void setApply_app_id(Integer apply_app_id) {
        this.apply_app_id = apply_app_id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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
