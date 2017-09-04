package com.advdsp.service.dsp.common.type;

/**
 */
public enum IndicatorDataType {

    Click_Nums("click_nums"),
    Distinct_Click_Nums("distinct_click_nums"),
    Activation_Nums("activation_nums"),
    Distinct_Activation_Nums("distinct_activation_nums");

    private String value;

    IndicatorDataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
