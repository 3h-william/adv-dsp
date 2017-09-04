package com.advdsp.service.send.consumer;

import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.common.type.ActionDataType;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.send.ServiceConfiguration;
import com.advdsp.service.send.db.dto.AppBaseConfigDto;
import com.advdsp.service.send.memory.AppBaseConfigInfo;
import com.advdsp.service.send.memory.SystemConfigInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.client.utils.URIBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.advdsp.service.dsp.common.type.ActionDataFieldNameType.*;
import static java.util.Objects.requireNonNull;

/**
 * TODO 线程池
 */
public class DspDataTransfer {
    protected static Logger logger = LoggerFactory.getLogger(DspDataTransfer.class.getName());

    private final int connectTimeout = Integer.parseInt(ServiceConfiguration.getInstance().getConfig().getString("dsp.data.transfer.connectTimeout", "3000"));
    private final int readTimeout = Integer.parseInt(ServiceConfiguration.getInstance().getConfig().getString("dsp.data.transfer.readTimeout", "3000"));
    private final boolean dsp_send_customer = ServiceConfiguration.getInstance().getConfig().getBoolean("dsp.send.customer", true);


    private String URL_CODE_CHARACTER = "UTF-8";

    /**
     * 将点击信息转发给广告主
     *
     * @param actionDataModel
     * @throws Throwable
     */
    public void clickDataTransfer(ActionDataModel actionDataModel) throws Throwable {
        if (!dsp_send_customer) {
            return;
        }
        try {
            URI uri = generateClickCallBackURI(actionDataModel);
            logger.info("[DspDataTransfer] clickDataTransfer url = " + uri.toString());
            WebTarget webTarget = generateHttpClient().target(uri);
            try {
                requestCheckWith200(uri, webTarget);
            } catch (Throwable t) {
                throw new TransferRequestException("request failed", t);
            }
        } catch (TransferRequestException e) {
            throw e;
        } catch (Throwable t) {
            throw new AdvDspException("clickDataTransfer failed  ", t);
        }
    }

    public void requestCheckWith200(URI uri, WebTarget webTarget) throws AdvDspException {
        Response response = getResponseWith200(uri, webTarget);
    }


    private void requestCheckWithValue(URI uri, WebTarget webTarget, String checkValue) throws AdvDspException {
        if (StringUtils.isEmpty(checkValue)) return;
        Response response = getResponseWith200(uri, webTarget);
        String responseBody = response.readEntity(String.class);
        if (checkValue.equalsIgnoreCase(responseBody)) {
            logger.info("response body ok = " + responseBody);
        } else {
            throw new AdvDspException("response body error = " + responseBody);
        }
    }

    public Response getResponseWith200(URI uri, WebTarget webTarget) throws AdvDspException {
        Invocation.Builder invocationBuilder = webTarget.request();
        Response response;
        try {
            response = invocationBuilder.get();
        } catch (Throwable t) {
            throw new AdvDspException("request error , url = " + uri.toString(), t);
        }
        if (301 == response.getStatus() || 302 == response.getStatus()) {
            logger.info("response body redirect = " + response.getStatus());
        }
        else if (200 != response.getStatus()) {
            throw new AdvDspException("data transfer error , response code = " + response.getStatus());
        }
        return response;
    }

    public void activationDataTransfer(ActionDataModel actionDataModel) throws Throwable {
        if (!dsp_send_customer) {
            return;
        }
        try {
            String activationCallBackUrl = requireNonNull(actionDataModel.getCallback(), "activationCallBackUrl is empty");
            URI uri = new URI(URLDecoder.decode(activationCallBackUrl, URL_CODE_CHARACTER));
            WebTarget webTarget = generateHttpClient().target(uri);
            requestCheckWith200(uri, webTarget);
        } catch (Throwable t) {
            throw new AdvDspException("clickDataTransfer failed  ", t);
        }

    }

    /**
     * <p>
     * http://example.com/event/notify?adid=1000043&channel=QiXing&ip=131.123.45.56&idfa=7B21AF38-2715-4073-A449-9475B90E2F73&callback=http%3a%2f%2fd.qixing.com%2fdev%2fapi%2fadnotify_ios_v_4.php%3fad_id%3d999c0a6d2e2f76a9576a+52a6a0bfcfa4%26idfa%3d7B21AF38-2715-4073-A449-9475B90E2F73%26atime%3d14093088+51%26ip%3d131.123.45.56%26notifytype%3d1&
     *
     * @param actionDataModel
     */

    public URI generateClickCallBackURI(ActionDataModel actionDataModel) throws Throwable {
        AppBaseConfigDto appBaseConfigDto = requireNonNull(AppBaseConfigInfo.getAppConfigInfoInstance().getAppBaseConfig(actionDataModel.getAppid()), "appConfigDto is empty");
        String appCallbackBase = requireNonNull(appBaseConfigDto.getCallback_url_base(), "appCallbackBase is empty");
        String systemCallBackBase = requireNonNull(SystemConfigInfo.getSystemConfigInfoInstance().getSystemCallBackUrlBase(appBaseConfigDto.getSystem_id()), "system callBackUrlBase is empty");
        String original_appCallbackBase = appCallbackBase; //兼容处理

        //第一步 构建激活时候的 callback url
        URIBuilder callBackUriBuilder = new URIBuilder(systemCallBackBase);
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
        String encodeCallBackUrl = URLEncoder.encode(callbackUrl, URL_CODE_CHARACTER);
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
            invokeCustomerUriBuilder.addParameter(callback.getValue(), callbackUrl);
        }
        if (!appCallbackBase.contains("&type=")) {
            invokeCustomerUriBuilder.addParameter(type.getValue(), ActionDataType.ClickSync.getValue());
        }
        return invokeCustomerUriBuilder.build();
    }

    public Client generateHttpClient() {
        ClientConfig clientConfig = new ClientConfig();
        // values are in milliseconds
        clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
        clientConfig.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
        clientConfig.property(ClientProperties.FOLLOW_REDIRECTS, true);
        //clientConfig.register(ObjectMapperProvider.class);
        return ClientBuilder.newClient(clientConfig);
    }
}
