package com.advdsp.service.send.rest.resources;

import com.advdsp.service.send.memory.AdxConfigInfo;
import com.advdsp.service.send.memory.AppBaseConfigInfo;
import com.advdsp.service.send.memory.SystemConfigInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;


@Path("/dsp")
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DspSendResources
        extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(DspSendResources.class.getName());

    @GET
    @Path("/config/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshConfig() {
        AppBaseConfigInfo.getAppConfigInfoInstance().loadAppConfig();
        SystemConfigInfo.getSystemConfigInfoInstance().loadSystemConfig();
        AdxConfigInfo.getAdxConfigInfoInstance().loadAdxConfig();
        return Response.ok().build();
    }
}
