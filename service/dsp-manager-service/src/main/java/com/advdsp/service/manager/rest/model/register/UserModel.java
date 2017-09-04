package com.advdsp.service.manager.rest.model.register;

/**
 */
public class UserModel {

    private String user_id;
    private String user_name;
    private String password;
    private boolean is_administrator=false;
    private boolean is_approve=false;


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

    public boolean isIs_administrator() {
        return is_administrator;
    }

    public void setIs_administrator(boolean is_administrator) {
        this.is_administrator = is_administrator;
    }

    public boolean isIs_approve() {
        return is_approve;
    }

    public void setIs_approve(boolean is_approve) {
        this.is_approve = is_approve;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", is_administrator=" + is_administrator +
                ", is_approve=" + is_approve +
                '}';
    }
}
