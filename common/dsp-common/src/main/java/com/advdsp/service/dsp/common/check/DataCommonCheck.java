package com.advdsp.service.dsp.common.check;

import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.model.ActionDataModel;
import org.apache.commons.lang3.StringUtils;

/**
 */
public class DataCommonCheck {

    /**
     * 检查收到的渠道发送来的点击数据，广告主发送的激活数据，是否符合要求
     * @param actionDataModel
     * @throws AdvDspException
     */
    public static void checkActionData(ActionDataModel actionDataModel) throws AdvDspException {
        if (null == actionDataModel) {
            throw new AdvDspException("actionDataModel is null");
        }

        if (StringUtils.isEmpty(actionDataModel.getType())) {
            throw new AdvDspException("type is empty");
        }

        if (StringUtils.isEmpty(actionDataModel.getAppid())) {
            throw new AdvDspException("appid is empty");
        }

        if (StringUtils.isEmpty(actionDataModel.getChannel())) {
            throw new AdvDspException("channel is empty");
        }

        if (StringUtils.isEmpty(actionDataModel.getTimestamp())) {
            throw new AdvDspException("timestamp is empty");
        }
    }

}
