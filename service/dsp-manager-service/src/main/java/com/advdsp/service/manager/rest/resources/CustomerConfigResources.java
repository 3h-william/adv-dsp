package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.CustomerConfigDto;
import com.advdsp.service.manager.rest.model.customer.CustomerConfigModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 广告主 相关接口
 */
@Path("/customer")
public class CustomerConfigResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(CustomerConfigResources.class.getName());

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAll() {
        logger.info("[customer:getAll] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<CustomerConfigModel> customerConfigModels = new ArrayList<>();
        try {
            List<CustomerConfigDto> customerConfigDtos = customerConfigService.getAll();
            if (null != customerConfigDtos) {
                for (CustomerConfigDto customerConfigDto : customerConfigDtos) {
                    customerConfigModels.add(parseCustomerConfigDtoToModel(customerConfigDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(customerConfigModels);
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
    public WrapResponseModel newObject(CustomerConfigModel customerConfigModel) {
        logger.info("[customer:new] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            CustomerConfigDto customerConfigDto = parseCustomerConfigModelToDto(customerConfigModel);
            customerConfigDto.setLast_edit_user_id(getUid());
            customerConfigDto.setLast_edit_date(new Date());
            customerConfigService.saveObject(customerConfigDto);
            wrapResponseModel.setData(parseCustomerConfigDtoToModel(customerConfigDto));
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[customer:new] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateObject(CustomerConfigModel customerConfigModel) {
        logger.info("[customer:update] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            CustomerConfigDto customerConfigDto = parseCustomerConfigModelToDto(customerConfigModel);
            customerConfigDto.setLast_edit_user_id(getUid());
            customerConfigDto.setLast_edit_date(new Date());
            customerConfigService.updateObjectById(customerConfigDto);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[customer:update] failed", t);
        }
        return wrapResponseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(CustomerConfigModel customerConfigModel) {
        logger.info("[customer:delete] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            customerConfigService.deleteById(customerConfigModel.getCustomer_id());
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[customer:delete] failed", t);
        }
        return wrapResponseModel;
    }
}
