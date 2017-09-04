package com.advdsp.service.receive.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.common.type.DataTypeUtils;
import com.advdsp.service.receive.business.BusinessFactory;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.receive.business.RequestBusiness;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.advdsp.service.dsp.common.code.ResponseCode.*;

@Path("/dsp")
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DspReceiveResources
        extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(DspReceiveResources.class.getName());
    private RequestBusiness requestBusiness = BusinessFactory.getRequestBusiness();

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel testOk() {
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setResultCode(1000);
        return wrapResponseModel;
    }

    @GET
    @Path("/faq")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel faq() {
        logger.info("faq invoke ok");
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setResultCode(1000);
        return wrapResponseModel;
    }

    /**
     * 程序主方法
     * type = ClickSync
     *
     * @param type
     * @param idfa
     * @param ip
     * @return
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel dspServiceRequest(@Context HttpServletRequest httpRequest, @QueryParam("type") String type, @QueryParam("idfa") String idfa, @QueryParam("ip") String ip
            , @QueryParam("callback") String callback, @QueryParam("channel") String channel
            , @QueryParam("clickKeyword") String clickKeyword, @QueryParam("appid") String appid, @QueryParam("click_timestamp") String click_timestamp) {

        ActionDataModel actionDataModel = new ActionDataModel();
        actionDataModel.setType(type);
        actionDataModel.setIdfa(idfa);
        actionDataModel.setIp(ip);
        actionDataModel.setCallback(callback);
        actionDataModel.setChannel(channel);
        actionDataModel.setClickKeyword(clickKeyword);
        actionDataModel.setAppid(appid);
        actionDataModel.setClick_timestamp(click_timestamp);
        actionDataModel.setTimestamp(String.valueOf(System.currentTimeMillis()));

        WrapResponseModel wrapResponseModel = new WrapResponseModel();

        try {
            if (DataTypeUtils.isClickSyncType(type)) {
                requestBusiness.clickSyncProcess(actionDataModel);
            } else if (DataTypeUtils.isCallBackType(type)) {
                requestBusiness.callBackProcess(actionDataModel);
            } else {
                throw new AdvDspException("dspServiceRequest , type is incorrect ,type = " + type);
            }
            wrapResponseModel.setResultCode(successCode);
        } catch (AdvDspException e) {
            wrapResponseModel.setResultCode(commonFailedCode);
            logger.error("dspServiceRequest failed , data = " + actionDataModel.toString(), e);
        } catch (Throwable t) {
            wrapResponseModel.setResultCode(commonFailedCode);
            logger.error("dspServiceRequest failed , data = " + actionDataModel.toString(), t);
        } finally {
            logger.info("dspServiceRequest requesthost=" + httpRequest.getHeader("X-Forwarded-For") + ", body=" + getUrl(httpRequest));
        }
        return wrapResponseModel;
    }

    public static String getUrl(HttpServletRequest req) {
        String reqUrl = req.getRequestURL().toString();
        String queryString = req.getQueryString();   // d=789
        if (queryString != null) {
            reqUrl += "?" + queryString;
        }
        return reqUrl;
    }

}
