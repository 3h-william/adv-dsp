package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.dsp.processor.kv.KVProcessorFactory;
import com.advdsp.service.manager.db.dto.SystemConfigDto;
import com.advdsp.service.manager.rest.model.system.SystemConfigModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统配置 相关接口
 */
@Path("/system")
public class SystemConfigResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(SystemConfigResources.class.getName());

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAll() {
        logger.info("[system:getAll] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<SystemConfigModel> systemConfigModels = new ArrayList<>();
        try {
            List<SystemConfigDto> systemConfigDtos = systemConfigService.getAll();
            if (null != systemConfigDtos) {
                for (SystemConfigDto systemConfigDto : systemConfigDtos) {
                    systemConfigModels.add(parseSystemConfigDtoToModel(systemConfigDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(systemConfigModels);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[system:getAll] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel newObject(SystemConfigModel systemConfigModel) {
        logger.info("[system:new] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            SystemConfigDto systemConfigDto = parseSystemConfigModelToDto(systemConfigModel);
            systemConfigDto.setLast_edit_user_id(getUid());
            systemConfigDto.setLast_edit_date(new Date());
            systemConfigService.saveObject(systemConfigDto);
            wrapResponseModel.setData(parseSystemConfigDtoToModel(systemConfigDto));
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[system:new] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateObject(SystemConfigModel systemConfigModel) {
        logger.info("[system:update] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            SystemConfigDto systemConfigDto = parseSystemConfigModelToDto(systemConfigModel);
            systemConfigDto.setLast_edit_date(new Date());
            systemConfigDto.setLast_edit_user_id(getUid());
            systemConfigService.updateObjectById(systemConfigDto);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[system:update] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(SystemConfigModel systemConfigModel) {
        logger.info("[system:delete] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            systemConfigService.deleteById(systemConfigModel.getSystem_id());
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[system:delete] failed", t);
        }
        return wrapResponseModel;
    }


    @GET
    @Path("/counter/fix")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel fixCounter(@QueryParam("rowkey") String rowkey) {
        logger.info("[system:fix] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            KVProcessorFactory.getInstance().mergeCounters(rowkey);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[system:fix] failed", t);
        }
        return wrapResponseModel;
    }

}
