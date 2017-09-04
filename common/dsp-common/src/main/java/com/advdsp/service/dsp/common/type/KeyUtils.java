package com.advdsp.service.dsp.common.type;

import com.advdsp.service.dsp.common.CommonUtils;
import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.model.ActionDataModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * 维护 kafka/ kV数据库中 key的生成和解析V
 * 统一 Kafka key 和 kV数据库的接口
 * key分为两部分 :
 * 1.  Key-basic  ==   Md5(X)_${KeyType}   其中 ， x为 idfa / ip中的一个
 * 2.  key-extra  ==   key的额外属性，由 "_" 做分割
 */
public class KeyUtils {

    private static FastDateFormat dsFormat = FastDateFormat.getInstance("yyyy-MM-dd");
    private static final String TYPE_IDFA = "idfa";
    private static final String TYPE_IP = "ip";


    /**
     * actionData key schema  :   md5(ip or idfa)_${type}_${rowType:ip or idfa}_${app_id}
     * <p>
     * example :
     *
     * @param actionDataModel
     * @return
     */
    public static String generateKeyWithNoTs(ActionDataModel actionDataModel) throws AdvDspException {
        // 优先取idfa的key
        if (StringUtils.isNoneEmpty(actionDataModel.getIdfa())) {
            return generateActionDataKey(CommonUtils.hashingMD5(actionDataModel.getIdfa()), actionDataModel.getType(), TYPE_IDFA, actionDataModel.getAppid(), null);
        }
        // 如果idfa没有， 则获取以ip为key
        else if (StringUtils.isNoneEmpty(actionDataModel.getIp())) {
            return generateActionDataKey(CommonUtils.hashingMD5(actionDataModel.getIp()), actionDataModel.getType(), TYPE_IP, actionDataModel.getAppid(), null);
        } else {
            throw new AdvDspException(" ip & idfa are both empty");
        }
    }


    /**
     * 到某一天的点击数据
     * actionData key schema  :  m5(ip or idfa)_${type}_${rowType:ip or idfa}_${app_id}_${channel_id}_${dsFormatStr}
     * <p>
     * example :
     *
     * @param actionDataModel
     * @return
     */
    public static String generateClickKeyWithDs(ActionDataModel actionDataModel) throws AdvDspException {
        if(StringUtils.isEmpty(actionDataModel.getChannel())){
            throw new IllegalArgumentException("channel is empty");
        }

        String dsFormatStr = dsFormat.format(Long.parseLong(actionDataModel.getTimestamp()));
        // 优先取idfa的key
        if (StringUtils.isNoneEmpty(actionDataModel.getIdfa())) {
            return generateClickActionDataKey(CommonUtils.hashingMD5(actionDataModel.getIdfa()),
                    actionDataModel.getType(), TYPE_IDFA, actionDataModel.getAppid(),actionDataModel.getChannel(), dsFormatStr);
        }
        // 如果idfa没有， 则获取以ip为key
        else if (StringUtils.isNoneEmpty(actionDataModel.getIp())) {
            return generateClickActionDataKey(CommonUtils.hashingMD5(actionDataModel.getIp()),
                    actionDataModel.getType(), TYPE_IP, actionDataModel.getAppid(),actionDataModel.getChannel(), dsFormatStr);
        } else {
            throw new AdvDspException(" ip & idfa are both empty");
        }
    }


    /**
     * actionData key schema  :   md5(ip or idfa)_${type}_${rowType:ip or idfa}_${app_id}_${ts}
     * <p>
     * example :
     *
     * @param actionDataModel
     * @return
     */
    public static String generateKeyWithTs(ActionDataModel actionDataModel) throws AdvDspException {
        // 优先取idfa的key
        if (StringUtils.isNoneEmpty(actionDataModel.getIdfa())) {
            return generateActionDataKey(CommonUtils.hashingMD5(actionDataModel.getIdfa()), actionDataModel.getType(), TYPE_IDFA, actionDataModel.getAppid(), actionDataModel.getTimestamp());
        }
        // 如果idfa没有， 则获取以ip为key
        else if (StringUtils.isNoneEmpty(actionDataModel.getIp())) {
            return generateActionDataKey(CommonUtils.hashingMD5(actionDataModel.getIp()), actionDataModel.getType(), TYPE_IP, actionDataModel.getAppid(), actionDataModel.getTimestamp());
        } else {
            throw new AdvDspException(" ip & idfa are both empty");
        }
    }

    private static String generateActionDataKey(String type, String md5, String rowType, String appId, String timeStamp) {
        if (StringUtils.isNoneEmpty(timeStamp)) {
            return StringUtils.join(new String[]{type, md5, rowType, appId, timeStamp}, "_");
        } else {
            return StringUtils.join(new String[]{type, md5, rowType, appId}, "_");
        }
    }

    private static String generateClickActionDataKey(String type, String md5, String rowType, String appId, String channelId, String timeStamp) {
        return StringUtils.join(new String[]{type, md5, rowType, appId, channelId, timeStamp}, "_");
    }

    /**
     * 根据key类型的定义 , 返回 kafka 中的key的类型,取分隔符"_"的第二个数据
     *
     * @param key
     * @return
     */
    public static String getKeyType(String key) throws Throwable {
        try {
            return StringUtils.split(key, "_")[1];
        } catch (Throwable t) {
            throw new AdvDspException("key is illegal , key = " + key);
        }
    }
}
