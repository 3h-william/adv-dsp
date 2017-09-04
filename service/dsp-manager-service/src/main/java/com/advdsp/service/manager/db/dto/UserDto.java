package com.advdsp.service.manager.db.dto;

/**
 * 用户配置
 */
public class UserDto {

    private String user_id;
    private String user_name;
    private String password;
    private Integer approve;
    private Integer is_administrator;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getApprove() {
        return approve;
    }

    public void setApprove(Integer approve) {
        this.approve = approve;
    }

    public Integer getIs_administrator() {
        return is_administrator;
    }

    public void setIs_administrator(Integer is_administrator) {
        this.is_administrator = is_administrator;
    }
}
