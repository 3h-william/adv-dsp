package com.advdsp.service.dsp.common.type;

/**
 */
public enum ActionDataType {
    ClickSync("ClickSync"), CallBack("CallBack") ,Activation("Activation");

    private String value;

    ActionDataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
