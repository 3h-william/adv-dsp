package com.advdsp.service.dsp.common.type;

import static com.advdsp.service.dsp.common.type.ActionDataType.*;

/**
 */
public class DataTypeUtils {

    /**
     * 是否广告点击数据类型
     * @param value
     * @return
     */
    public static boolean isClickSyncType(String value){
        return ClickSync.getValue().equals(value);
    }

    /**
     * 是否广告主回调类型
     * @param value
     * @return
     */
    public static boolean isCallBackType(String value){
        return CallBack.getValue().equals(value);
    }

}
