package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.AppConfigDto;
import com.advdsp.service.manager.rest.model.app.AppConfigModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * App 相关接口
 */
@Path("/app")

public class AppConfigResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(AppConfigResources.class.getName());

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAll() {
        logger.info("[app:getAll] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<AppConfigModel> appBaseConfigModels = new ArrayList<>();
        try {
            List<AppConfigDto> appBaseConfigDtos = appConfigService.getAll();
            if (null != appBaseConfigDtos) {
                for (AppConfigDto appBaseConfigDto : appBaseConfigDtos) {
                    appBaseConfigModels.add(parseAppConfigDtoToModel(appBaseConfigDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(appBaseConfigModels);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[app:getAll] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel newObject(AppConfigModel appConfigModel) {
        logger.info("[app:new] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            AppConfigDto appConfigDto = parseAppConfigModelToDto(appConfigModel);
            appConfigDto.setLast_edit_date(new Date());
            appConfigDto.setLast_edit_user_id(getUid());
            appConfigService.saveObject(appConfigDto);
            wrapResponseModel.setData(parseAppConfigDtoToModel(appConfigDto));
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[app:new] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateObject(AppConfigModel appConfigModel) {
        logger.info("[app:update] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            AppConfigDto appConfigDto = parseAppConfigModelToDto(appConfigModel);
            appConfigDto.setLast_edit_date(new Date());
            appConfigDto.setLast_edit_user_id(getUid());
            appConfigService.updateObjectById(appConfigDto);
            wrapResponseModel.setData(parseAppConfigDtoToModel(appConfigDto));
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[app:update] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(AppConfigModel appConfigModel) {
        logger.info("[app:delete] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            appConfigService.deleteById(appConfigModel.getApp_id());
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[app:delete] failed", t);
        }
        return wrapResponseModel;
    }


    @GET
    @Path("/getNoAdxConfig")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getNoAdxConfig(@QueryParam("channel_id") String channel_id, @QueryParam("app_id") String app_id) {
        System.out.println("auth="+requestContext.getProperty("ccc"));
        logger.info("[app:getNoAdxConfig] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<AppConfigModel> appBaseConfigModels = new ArrayList<>();
        try {
            if (null == channel_id) {
                throw new IllegalArgumentException("channel_id is empty");
            }
            if (null == app_id) {
                throw new IllegalArgumentException("app_id is empty");
            }
            List<AppConfigDto> appBaseConfigDtos = appConfigService.getNoAdxConfig(Integer.parseInt(channel_id), Integer.parseInt(app_id));
            if (null != appBaseConfigDtos) {
                for (AppConfigDto appBaseConfigDto : appBaseConfigDtos) {
                    appBaseConfigModels.add(parseAppConfigDtoToModel(appBaseConfigDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(appBaseConfigModels);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[app:getNoAdxConfig] failed", t);
        }
        return wrapResponseModel;
    }
}
