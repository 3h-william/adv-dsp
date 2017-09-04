package com.advdsp.service.send;

import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.send.consumer.DspDataTransfer;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.Test;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.advdsp.service.dsp.common.type.ActionDataType.*;

/**
 */
public class TestCallBackUrl {
    public static void main(String[] args) {
        Map<String, String> values = new HashMap<String, String>();
        values.put("value", "a");
        values.put("column", "b");
        values.put("dddd", "fff");
        StrSubstitutor sub = new StrSubstitutor(values, "%(", ")");
        String result = sub.replace("There's an incorrect value '%(value)' in column # %(column)");
        //System.out.println(result);

    }

    @Test
    public void testResponse() throws URISyntaxException, AdvDspException {
        DspDataTransfer dspDataTransfer = new DspDataTransfer();
        String url = "http://tracking.vlion.cn/click/?q=32&idfa=FEF3A0FD-14EB-4A31-A554-35B0B5C8A8A9&callback=http%3A%2F%2Felb-dataservice-2057490143.cn-north-1.elb.amazonaws.com.cn%3A8310%2Fdsp%3Ftype%3DCallBack%26appid%3D452176796%26channel%3D0%26idfa%3DFEF3A0FD-14EB-4A31-A554-35B0B5C8A8A9%26click_timestamp%3D1501573405223&type=ClickSync";
        url = "http://tracking.vlion.cn:8082/media?q=32&idfa=7A0856A2-8321-45CA-9342-78CC2C031079&callback=http%3A%2F%2" +
                "Felb-dataservice-2057490143.cn-north-1.elb.amazonaws.com.cn%3A8310%2Fdsp%3Ftype%3DCallBack%26appid%3D452176796%26channel%3D0%26idfa%3D7A0856A2-8321-45CA-93" +
                "42-78CC2C031079%26click_timestamp%3D1501581735699&type=ClickSync";
        URI uri = new URI(url);
        WebTarget webTarget = dspDataTransfer.generateHttpClient().target(uri);
        Response response2 =webTarget.property(ClientProperties.FOLLOW_REDIRECTS, true).request().get();
        Invocation.Builder invocationBuilder = webTarget.request();
        Response response;
        try {
            response = invocationBuilder.get();
        } catch (Throwable t) {
            throw new AdvDspException("request error , url = " + uri.toString(), t);
        }

        if (301 == response.getStatus() || 302 == response.getStatus()) {
//
        }

        if (200 != response.getStatus()) {
            throw new AdvDspException("data transfer error , response code = " + response.getStatus());
        }
        //dspDataTransfer.requestCheckWith200(uri, webTarget);
    }

    @Test
    public void testClickCallBackUrl() throws Throwable {
        DspDataTransfer dspDataTransfer = new DspDataTransfer();
        ActionDataModel actionDataModel = new ActionDataModel();
        actionDataModel.setType(ClickSync.getValue());
        //actionDataModel.setIp("localhost");
        actionDataModel.setIdfa("test-idfa");
        actionDataModel.setChannel("12345");
        actionDataModel.setAppid("100002");
        actionDataModel.setTimestamp(String.valueOf(System.currentTimeMillis()));
       URI x = dspDataTransfer.generateClickCallBackURI(actionDataModel);
//        dspDataTransfer.clickDataTransfer(actionDataModel);

        System.out.println(x.toString());
    }


    /**
     * http://47.92.54.123:8080/QiXing/dsp?type=Activation&adid=${ad_id}&idfa=${idfa}&ip=${ipaddr}&channel=${channelid}
     * @throws Throwable
     */
    @Test
    public void testActivationCallBackUrl() throws Throwable {
        DspDataTransfer dspDataTransfer = new DspDataTransfer();
        ActionDataModel actionDataModel = new ActionDataModel();
        actionDataModel.setCallback(URLEncoder.encode("http://localhost:9000/dsp?a=a1&b=b2", "UTF-8"));
        dspDataTransfer.activationDataTransfer(actionDataModel);
    }

    /**
     * http://47.92.54.123:8080/QiXing/dsp?type=Activation&adid=${ad_id}&idfa=${idfa}&ip=${ipaddr}&channel=${channelid}
     * @throws Throwable
     */
    @Test
    public void urlEncode() throws Throwable {
        String url = "http://localhost:9000/dsp?a=a1&b=b2";
        url = "http://localhost:8310?idfa=test-idfa&ip=localhost&type=Activation&click_timestamp=1499529970550";
        System.out.println(URLEncoder.encode(url, "UTF-8"));
    }

    @Test
    public void urlDecode() throws Throwable {
        String url = "http%253A%252F%252Flocalhost%253A8310%253Fidfa%253Didfa-test-123456766ee6%2526ip%253Dlocalhost%2526type%253DActivation%2526click_timestamp%253D1499529384779";
        //url = "http%3A%2F%2Flocalhost%3A9000%2Fdsp%3Fa%3Da1%26b%3Db2";
        System.out.println(URLDecoder.decode(url,"UTF-8"));
    }



}


