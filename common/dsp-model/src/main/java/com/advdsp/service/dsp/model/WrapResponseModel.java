package com.advdsp.service.dsp.model;

@javax.xml.bind.annotation.XmlRootElement
public class WrapResponseModel {
    //manager使用
    private Integer code;
    // 对外使用
    private Integer resultCode;
    private String message;
    private Object data;
    private String debug;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}