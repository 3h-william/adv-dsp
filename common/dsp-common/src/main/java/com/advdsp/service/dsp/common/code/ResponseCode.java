package com.advdsp.service.dsp.common.code;

/**
 * 服务response code
 *
 *
 * 成功                 {"resultCode":1000}
 * 普通错误     		    {"resultCode":1001}
 * 非法的广告Id       	{"resultCode":1002}
 * 同步到广告主失败        {"resultCode":1003}
 */
public class ResponseCode {

    public static final int successCode = 1000;
    public static final int commonFailedCode = 1001;
    public static final int illegalAdvCode = 1002;
    public static final int syncFailedCode = 1003;
}
