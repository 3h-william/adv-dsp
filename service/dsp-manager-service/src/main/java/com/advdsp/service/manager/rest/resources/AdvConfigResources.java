package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.common.type.ActionDataType;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.AdvConfigDto;
import com.advdsp.service.manager.rest.model.adv.AdvConfigModel;
import com.advdsp.service.manager.rest.model.adv.MockModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;

import static com.advdsp.service.dsp.common.type.ActionDataFieldNameType.*;
import static com.advdsp.service.dsp.common.type.ActionDataFieldNameType.ip;
import static com.advdsp.service.dsp.common.type.ActionDataType.ClickSync;
import static java.util.Objects.requireNonNull;

/**
 * 广告主 相关接口
 */
@Path("/adv")
public class AdvConfigResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(AdvConfigResources.class.getName());

//    /**
//     * 生成提供给渠道方去配置的我方调用诶之
//     */
//    private String generateCallBack(AdvConfigDto advConfigDto) throws URISyntaxException {
//        if (StringUtils.isEmpty(advConfigDto.getSystem_callback())) {
//            return null;
//        }
//        try {
//            URIBuilder callBackUriBuilder = new URIBuilder(advConfigDto.getSystem_callback());
//            callBackUriBuilder.addParameter(appid.getValue(), String.valueOf(advConfigDto.getApp_id()));
//            callBackUriBuilder.addParameter(channel.getValue(), String.valueOf(advConfigDto.getChannel_id()));
//            callBackUriBuilder.addParameter(adid.getValue(), String.valueOf(advConfigDto.getAdv_id()));
//            return callBackUriBuilder.build().toString();
//        } catch (Throwable t) {
//            logger.warn("[adv:generateCallBack] failed . ", t);
//            return null;
//        }
//    }

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAll() {
        logger.info("[adv:getAll] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<AdvConfigModel> advConfigModels = new ArrayList<>();
        try {
            List<AdvConfigDto> advConfigDtos = advConfigService.getAll();
            if (null != advConfigDtos) {
                for (AdvConfigDto advConfigDto : advConfigDtos) {
                    AdvConfigModel advConfigModel = parseAdvConfigDtoToModel(advConfigDto);
                    advConfigModel.setCallback(generateChannelClickUrl(advConfigDto, "idfa-test", "ip-test"));
                    advConfigModels.add(advConfigModel);
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(advConfigModels);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[channel:getAll] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel newObject(AdvConfigModel advConfigModel) {
        logger.info("[adv:new] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            AdvConfigDto advConfigDto = parseAdvConfigModelToDto(advConfigModel);
            advConfigDto.setLast_edit_date(new Date());
            advConfigDto.setLast_edit_user_id(getUid());
            advConfigService.saveObject(advConfigDto);
            wrapResponseModel.setData(parseAdvConfigDtoToModel(advConfigDto));
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            if (t.getMessage().contains("DuplicateKeyException")) {
                wrapResponseModel.setMessage("已经存在");
            }
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adv:new] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateObject(AdvConfigModel advConfigModel) {
        logger.info("[adv:update] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            AdvConfigDto advConfigDto = parseAdvConfigModelToDto(advConfigModel);
            advConfigDto.setLast_edit_date(new Date());
            advConfigDto.setLast_edit_user_id(getUid());
            advConfigService.updateObjectById(advConfigDto);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            if (t.getMessage().contains("DuplicateKeyException")) {
                wrapResponseModel.setMessage("已经存在");
            }
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adv:update] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(AdvConfigModel advConfigModel) {
        logger.info("[adv:delete] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            advConfigService.deleteById(advConfigModel.getAdv_id());
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adv:delete] failed", t);
        }
        return wrapResponseModel;
    }


    @GET
    @Path("/mock/{adv_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel mock(@PathParam("adv_id") Integer adv_id, @QueryParam("idfa") String idfa, @QueryParam("ip") String ip) {
        logger.info("[adv:mock] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            MockModel mockModel = new MockModel();
            AdvConfigDto advConfigDto = advConfigService.getObjectByID(adv_id);
            if (null == advConfigDto) {
                throw new IllegalArgumentException("adv is not exist");
            }
            ActionDataModel actionDataModel = new ActionDataModel();
            actionDataModel.setType(ClickSync.getValue());
            actionDataModel.setIp(ip);
            actionDataModel.setIdfa(idfa);
            actionDataModel.setChannel(String.valueOf(advConfigDto.getChannel_id()));
            actionDataModel.setAppid(String.valueOf(advConfigDto.getApp_id()));
            actionDataModel.setTimestamp(String.valueOf(System.currentTimeMillis()));
            String appActiveUrl = generateActiveCallBackUrl(actionDataModel, advConfigDto.getSystem_callback());
            String appClickUrl = generateClickCallBackUrl(actionDataModel, advConfigDto.getApp_callback(), appActiveUrl);
            String channelClickUrl = generateChannelClickUrl(advConfigDto, idfa, ip);
            mockModel.setApp_click_url(appClickUrl);
            mockModel.setApp_active_url(appActiveUrl);
            mockModel.setChannel_click_url(channelClickUrl);
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(mockModel);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adv:mock] failed", t);
        }
        return wrapResponseModel;
    }


    private String generateChannelClickUrl(AdvConfigDto advConfigDto, String idfaStr, String ipStr) {
        try{
            URIBuilder callBackUriBuilder = new URIBuilder(advConfigDto.getSystem_callback());
            callBackUriBuilder.addParameter(appid.getValue(), String.valueOf(advConfigDto.getApp_id()));
            callBackUriBuilder.addParameter(channel.getValue(), String.valueOf(advConfigDto.getChannel_id()));
            callBackUriBuilder.addParameter(adid.getValue(), String.valueOf(advConfigDto.getAdv_id()));
            callBackUriBuilder.addParameter(type.getValue(), ActionDataType.ClickSync.getValue());
            callBackUriBuilder.addParameter(idfa.getValue(), idfaStr);
            callBackUriBuilder.addParameter(ip.getValue(), ipStr);
            return callBackUriBuilder.build().toString();
        }catch (Throwable t){
            logger.error("generateChannelClickUrl failed",t);
            return null;
        }

    }

    private String generateActiveCallBackUrl(ActionDataModel actionDataModel, String systemCallback) throws URISyntaxException {
        //第一步 构建激活时候的 callback url
        URIBuilder callBackUriBuilder = new URIBuilder(systemCallback);
        callBackUriBuilder.addParameter(type.getValue(), ActionDataType.CallBack.getValue());
        callBackUriBuilder.addParameter(appid.getValue(), actionDataModel.getAppid());
        callBackUriBuilder.addParameter(channel.getValue(), actionDataModel.getChannel());
        if (StringUtils.isNoneEmpty(actionDataModel.getIdfa())) {
            callBackUriBuilder.addParameter(idfa.getValue(), actionDataModel.getIdfa());
        }
        if (StringUtils.isNoneEmpty(actionDataModel.getIp())) {
            callBackUriBuilder.addParameter(ip.getValue(), actionDataModel.getIp());
        }
        callBackUriBuilder.addParameter(click_timestamp.getValue(), actionDataModel.getTimestamp());

        String callbackUrl = callBackUriBuilder.toString();
        return callbackUrl;
    }

    private String generateClickCallBackUrl(ActionDataModel actionDataModel, String appCallBack, String activeCallBackUrl)
            throws URISyntaxException, UnsupportedEncodingException {
        String appCallbackBase = appCallBack;
        String original_appCallbackBase = appCallBack; //兼容处理

        // 第二步(a) 替换自定义参数
        Map<String, String> values = new HashMap<String, String>();
        final String idfaPlaceHolderStr = idfa.getValue();
        final String ipPlaceHolderStr = ip.getValue();
        final String callbackPlaceHolderStr = callback.getValue();
        if (StringUtils.isNoneEmpty(actionDataModel.getIdfa())) {
            values.put(idfaPlaceHolderStr, actionDataModel.getIdfa());
        } else {
            values.put(idfaPlaceHolderStr, "");
        }
        if (StringUtils.isNoneEmpty(actionDataModel.getIp())) {
            values.put(ipPlaceHolderStr, actionDataModel.getIp());
        } else {
            values.put(ipPlaceHolderStr, "");
        }
        String encodeCallBackUrl = URLEncoder.encode(activeCallBackUrl, "UTF-8");
        values.put(callbackPlaceHolderStr, encodeCallBackUrl);
        StrSubstitutor sub = new StrSubstitutor(values, "{", "}");
        appCallbackBase = sub.replace(appCallbackBase);


        URIBuilder invokeCustomerUriBuilder = new URIBuilder(appCallbackBase);
        // 第二步(b)，直接追加参数，兼容 旧格式
        if (!original_appCallbackBase.contains("{idfa}")) {
            if (StringUtils.isNoneEmpty(actionDataModel.getIdfa())) {
                invokeCustomerUriBuilder.addParameter(idfa.getValue(), actionDataModel.getIdfa());
            }
            if (StringUtils.isNoneEmpty(actionDataModel.getIp())) {
                invokeCustomerUriBuilder.addParameter(ip.getValue(), actionDataModel.getIp());
            }
            invokeCustomerUriBuilder.addParameter(callback.getValue(), activeCallBackUrl);
        }
        if (!appCallbackBase.contains("&type=")) {
            invokeCustomerUriBuilder.addParameter(type.getValue(), ActionDataType.ClickSync.getValue());
        }
        return invokeCustomerUriBuilder.build().toString();
    }
}
