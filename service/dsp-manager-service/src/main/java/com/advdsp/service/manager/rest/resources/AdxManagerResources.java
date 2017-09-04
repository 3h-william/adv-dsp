package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.AdxConfigDto;
import com.advdsp.service.manager.db.dto.AppChannelMappingDto;
import com.advdsp.service.manager.rest.model.adx.AppChannelMappingModel;
import com.advdsp.service.manager.rest.model.adx.AdxConfigModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Adx 相关接口
 */
@Path("/adx")
public class AdxManagerResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(AdxManagerResources.class.getName());

    @GET
    @Path("/searchCopyConfigMapping")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel searchCopyConfigMapping(@QueryParam("channel_id") String channel_id, @QueryParam("copy_app_id") String copy_app_id) {
        logger.info("[adx:searchCopyConfigMapping] channel_id = " + channel_id + ", copy_app_id = " + copy_app_id + " uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<AdxConfigModel> adxConfigMappingModels = new ArrayList<>();
        try {
            //check
            if (StringUtils.isEmpty(channel_id)) {
                throw new IllegalArgumentException("channel_id is empty");
            }
            if (StringUtils.isEmpty(copy_app_id)) {
                throw new IllegalArgumentException("copy_app_id is empty");
            }
            List<AdxConfigDto> adxConfigMappingDtos = adxConfigManagerService.searchAdxConfigMapping(channel_id, copy_app_id);
            if (null != adxConfigMappingDtos) {
                for (AdxConfigDto adxConfigMappingDto : adxConfigMappingDtos) {
                    adxConfigMappingModels.add(parseAdxConfigDtoToModel(adxConfigMappingDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(adxConfigMappingModels);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adx:searchCopyConfigMapping] failed", t);
        }
        return wrapResponseModel;
    }


    @GET
    @Path("/searchAppConfigMapping")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel searchAppConfigMapping(@QueryParam("channel_id") String channel_id, @QueryParam("app_id") String app_id) {
        logger.info("[adx:searchAppConfigMapping] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<AppChannelMappingModel> appChannelMappingModels = new ArrayList<>();
        try {
            List<AppChannelMappingDto> appChannelMappingDtos = adxConfigManagerService.searchAppConfigMapping(channel_id, app_id);
            if (null != appChannelMappingDtos) {
                for (AppChannelMappingDto appChannelMappingDto : appChannelMappingDtos) {
                    appChannelMappingModels.add(parseAppChannelMappingDtoToModel(appChannelMappingDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(appChannelMappingDtos);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adx:searchAppConfigMapping] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel newAdxConfig(AdxConfigModel adxConfigModel) {
        logger.info("[adx:newAdxConfig] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {

            AdxConfigDto adxConfigDto = parseAdxConfigModelToDto(adxConfigModel);
            adxConfigDto.setLast_edit_user_id(getUid());
            adxConfigDto.setLast_edit_date(new Date());
            adxConfigManagerService.saveObject(adxConfigDto);
            wrapResponseModel.setData(adxConfigDto);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            if(StringUtils.contains(wrapResponseModel.getDebug(),"Duplicate")){
                wrapResponseModel.setMessage("重复插入");
            }
            logger.error("[adx:newAdxConfig] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateAdxConfig(AdxConfigModel adxConfigModel) {
        logger.info("[adx:updateAdxConfig] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            AdxConfigDto adxConfigDto = parseAdxConfigModelToDto(adxConfigModel);
            adxConfigDto.setLast_edit_user_id(getUid());
            adxConfigDto.setLast_edit_date(new Date());
            adxConfigManagerService.updateObjectById(adxConfigDto);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adx:updateAdxConfig] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(AdxConfigModel adxConfigModel) {
        logger.info("[adx:delete]  uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            adxConfigManagerService.deleteById(adxConfigModel.getAdx_id());
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adx:delete] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/deleteAllMapping")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel deleteAllMapping(AdxConfigModel adxConfigModel) {
        logger.info("[adx:deleteAllMapping]  uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            if(null==adxConfigModel.getChannel_id()){
                throw new IllegalArgumentException("channel id is empty");
            }
            if(null==adxConfigModel.getCopy_app_id()){
                throw new IllegalArgumentException("copy app id is empty");
            }
            adxConfigManagerService.deleteByChannelIdAndCopyAppId(adxConfigModel.getChannel_id(),adxConfigModel.getCopy_app_id());
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[adx:deleteAllMapping] failed", t);
        }
        return wrapResponseModel;
    }
}
