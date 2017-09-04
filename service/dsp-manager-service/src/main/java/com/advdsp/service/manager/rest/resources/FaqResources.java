package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 */
@Path("/faq")
public class FaqResources {
    private static Logger logger = LoggerFactory.getLogger(FaqResources.class.getName());

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel faq() {
        logger.info("faq invoke ok");
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setCode(0);
        return wrapResponseModel;
    }
}
