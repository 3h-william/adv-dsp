package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.ChannelConfigDto;
import com.advdsp.service.manager.rest.model.channel.ChannelConfigModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 渠道相关接口
 */
@Path("/channel")
public class ChannelConfigResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(ChannelConfigResources.class.getName());

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAll() {
        logger.info("[channel:getAll] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<ChannelConfigModel> channelConfigModels = new ArrayList<>();
        try {
            List<ChannelConfigDto> channelConfigDtos = channelConfigService.getAll();
            if (null != channelConfigDtos) {
                for (ChannelConfigDto channelConfigDto : channelConfigDtos) {
                    channelConfigModels.add(parseChannelConfigDtoToModel(channelConfigDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(channelConfigModels);
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
    public WrapResponseModel newObject(ChannelConfigModel channelConfigModel) {
        logger.info("[channel:new] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            ChannelConfigDto channelConfigDto = parseChannelConfigModelToDto(channelConfigModel);
            channelConfigDto.setLast_edit_date(new Date());
            channelConfigDto.setLast_edit_user_id(getUid());
            channelConfigService.saveObject(channelConfigDto);
            wrapResponseModel.setData(parseChannelConfigDtoToModel(channelConfigDto));
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[channel:new] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateObject(ChannelConfigModel channelConfigModel) {
        logger.info("[channel:update] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            ChannelConfigDto channelConfigDto = parseChannelConfigModelToDto(channelConfigModel);
            channelConfigDto.setLast_edit_user_id(getUid());
            channelConfigDto.setLast_edit_date(new Date());
            channelConfigService.updateObjectById(channelConfigDto);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[channel:update] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(ChannelConfigModel channelConfigModel) {
        logger.info("[channel:delete] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            channelConfigService.deleteById(channelConfigModel.getChannel_id());
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[channel:delete] failed", t);
        }
        return wrapResponseModel;
    }
}
