package com.advdsp.service.dsp.common.type;

/**
 */
public enum ActionDataFieldNameType {

    type("type"),
    idfa("idfa"),
    ip("ip"),
    callback("callback"),
    adid("adid"),
    channel("channel"),
    clickKeyword("clickKeyword"),
    appid("appid"),
    click_timestamp("click_timestamp");

    private String value;

    ActionDataFieldNameType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
