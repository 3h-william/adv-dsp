package com.advdsp.service.dsp.common;

import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.common.type.KeyUtils;
import com.advdsp.service.dsp.model.ActionDataModel;

import static com.advdsp.service.dsp.common.type.ActionDataType.ClickSync;

/**
 */
public class TestKey {
    public static void main(String[] args) throws AdvDspException {
        ActionDataModel actionDataModel = new ActionDataModel();
        actionDataModel.setType(ClickSync.getValue());
        //actionDataModel.setIp("localhost");
        actionDataModel.setIdfa("test-idfa");
        actionDataModel.setChannel("12345");
        actionDataModel.setAppid("100002");
        actionDataModel.setTimestamp(System.currentTimeMillis()+"");

        String key = KeyUtils.generateClickKeyWithDs(actionDataModel);
        System.out.println(key);

    }
}
