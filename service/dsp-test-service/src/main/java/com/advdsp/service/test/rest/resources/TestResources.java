package com.advdsp.service.test.rest.resources;


import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.test.ServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import static com.advdsp.service.dsp.common.code.ResponseCode.*;

@Path("/dsp")
@XmlRootElement
public class TestResources {

    private static Logger logger = LoggerFactory.getLogger(ServiceConfiguration.class.getName());


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel testOk(@Context HttpServletRequest httpRequest) {
        logger.info("dsp request : " + getUrl(httpRequest));
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setResultCode(successCode);
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