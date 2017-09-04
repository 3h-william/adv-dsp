package com.advdsp.service.dsp.common.serialize;

import com.advdsp.service.dsp.model.ActionDataModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


/**
 * 所有数据的序列化处理
 */
public class DataSerialize {
    private static ObjectMapper jsonMapper = new ObjectMapper();

    public static String serializeActivationData(ActionDataModel actionDataModel) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(actionDataModel);
    }


    public static ActionDataModel deserializeActivationData(String json) throws IOException {
        return jsonMapper.readValue(json, ActionDataModel.class);
    }
}
