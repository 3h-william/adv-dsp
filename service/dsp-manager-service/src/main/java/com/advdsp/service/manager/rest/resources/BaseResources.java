package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.manager.ServiceContextFactory;
import com.advdsp.service.manager.db.dto.*;
import com.advdsp.service.manager.rest.model.adv.AdvConfigModel;
import com.advdsp.service.manager.rest.model.adx.AppChannelMappingModel;
import com.advdsp.service.manager.rest.model.adx.AdxConfigModel;
import com.advdsp.service.manager.rest.model.app.AppConfigModel;
import com.advdsp.service.manager.rest.model.channel.ChannelConfigModel;
import com.advdsp.service.manager.rest.model.customer.CustomerConfigModel;
import com.advdsp.service.manager.rest.model.register.UserModel;
import com.advdsp.service.manager.rest.model.system.SystemConfigModel;
import com.advdsp.service.manager.service.*;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

public class BaseResources {

    protected AdxConfigManagerService adxConfigManagerService = ServiceContextFactory.adxConfigManagerService;
    protected AppConfigService appConfigService = ServiceContextFactory.appConfigService;
    protected ChannelConfigService channelConfigService = ServiceContextFactory.channelConfigService;
    protected CustomerConfigService customerConfigService = ServiceContextFactory.customerConfigService;
    protected SystemConfigService systemConfigService = ServiceContextFactory.systemConfigService;
    protected AdvConfigService advConfigService = ServiceContextFactory.advConfigService;
    protected AdvStatisticService advStatisticService = ServiceContextFactory.advStatisticService;
    protected UserService userService = ServiceContextFactory.userService;
    protected TokenService tokenService = ServiceContextFactory.tokenService;


    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;

    protected String getUid() {
        return requestContext.getHeaderString("uid");
    }

    protected AppConfigModel parseAppConfigDtoToModel(AppConfigDto appConfigDto) {
        AppConfigModel appConfigModel = new AppConfigModel();
        appConfigModel.setApp_id(appConfigDto.getApp_id());
        appConfigModel.setCustomer_id(appConfigDto.getCustomer_id());
        appConfigModel.setCallback_url_base(appConfigDto.getCallback_url_base());
        appConfigModel.setSystem_id(appConfigDto.getSystem_id());
        appConfigModel.setApp_name(appConfigDto.getApp_name());
        appConfigModel.setApp_describe(appConfigDto.getApp_describe());
        if (null != appConfigDto.getLast_edit_date()) {
            appConfigModel.setLast_edit_date(appConfigDto.getLast_edit_date().getTime());
        }
        appConfigModel.setLast_edit_user_id(appConfigDto.getLast_edit_user_id());
        appConfigModel.setLast_edit_user_name(appConfigDto.getLast_edit_user_name());
        appConfigModel.setCustomer_name(appConfigDto.getCustomer_name());
        appConfigModel.setSystem_name(appConfigDto.getSystem_name());
        return appConfigModel;
    }


    protected AppConfigDto parseAppConfigModelToDto(AppConfigModel appConfigModel) {
        AppConfigDto appConfigDto = new AppConfigDto();
        appConfigDto.setApp_id(appConfigModel.getApp_id());
        appConfigDto.setCustomer_id(appConfigModel.getCustomer_id());
        appConfigDto.setCallback_url_base(appConfigModel.getCallback_url_base());
        appConfigDto.setSystem_id(appConfigModel.getSystem_id());
        appConfigDto.setApp_name(appConfigModel.getApp_name());
        appConfigDto.setApp_describe(appConfigModel.getApp_describe());
        appConfigDto.setLast_edit_user_id(appConfigModel.getLast_edit_user_id());
        appConfigDto.setLast_edit_user_name(appConfigModel.getLast_edit_user_name());
        return appConfigDto;
    }


    protected ChannelConfigModel parseChannelConfigDtoToModel(ChannelConfigDto channelConfigDto) {
        ChannelConfigModel channelConfigModel = new ChannelConfigModel();
        channelConfigModel.setChannel_id(channelConfigDto.getChannel_id());
        channelConfigModel.setChannel_name(channelConfigDto.getChannel_name());
        channelConfigModel.setChannel_describe(channelConfigDto.getChannel_describe());
        if (null != channelConfigDto.getLast_edit_date()) {
            channelConfigModel.setLast_edit_date(channelConfigDto.getLast_edit_date().getTime());
        }
        channelConfigModel.setLast_edit_user_id(channelConfigDto.getLast_edit_user_id());
        channelConfigModel.setLast_edit_user_name(channelConfigDto.getLast_edit_user_name());
        return channelConfigModel;
    }

    protected ChannelConfigDto parseChannelConfigModelToDto(ChannelConfigModel channelConfigModel) {
        ChannelConfigDto channelConfigDto = new ChannelConfigDto();
        channelConfigDto.setChannel_id(channelConfigModel.getChannel_id());
        channelConfigDto.setChannel_name(channelConfigModel.getChannel_name());
        channelConfigDto.setChannel_describe(channelConfigModel.getChannel_describe());
        channelConfigDto.setLast_edit_user_id(channelConfigModel.getLast_edit_user_id());
        channelConfigDto.setLast_edit_user_name(channelConfigModel.getLast_edit_user_name());
        return channelConfigDto;
    }

    protected AppChannelMappingModel parseAppChannelMappingDtoToModel(AppChannelMappingDto appChannelMappingDto) {
        AppChannelMappingModel appChannelMappingModel = new AppChannelMappingModel();
        appChannelMappingModel.setApp_id(appChannelMappingDto.getApp_id());
        appChannelMappingModel.setApp_name(appChannelMappingDto.getApp_name());
        appChannelMappingModel.setChannel_id(appChannelMappingDto.getChannel_id());
        appChannelMappingModel.setChannel_name(appChannelMappingDto.getChannel_name());
        if (null == appChannelMappingDto.getCount()) {
            appChannelMappingDto.setCount(0);// 默认
        } else {
            appChannelMappingModel.setCount(appChannelMappingDto.getCount());
        }
        return appChannelMappingModel;
    }

    protected AdxConfigModel parseAdxConfigDtoToModel(AdxConfigDto adxConfigManagerDto) {
        AdxConfigModel adxConfigManagerModel = new AdxConfigModel();
        adxConfigManagerModel.setAdx_id(adxConfigManagerDto.getAdx_id());
        adxConfigManagerModel.setChannel_id(adxConfigManagerDto.getChannel_id());
        adxConfigManagerModel.setCopy_app_id(adxConfigManagerDto.getCopy_app_id());
        adxConfigManagerModel.setApply_app_id(adxConfigManagerDto.getApply_app_id());
        adxConfigManagerModel.setAmount(adxConfigManagerDto.getAmount());
        adxConfigManagerModel.setChannel_name(adxConfigManagerDto.getChannel_name());
        adxConfigManagerModel.setCopy_app_name(adxConfigManagerDto.getCopy_app_name());
        adxConfigManagerModel.setApply_app_name(adxConfigManagerDto.getApply_app_name());
        adxConfigManagerModel.setLast_edit_user_id(adxConfigManagerDto.getLast_edit_user_id());
        if (null != adxConfigManagerDto.getLast_edit_date()) {
            adxConfigManagerModel.setLast_edit_date(adxConfigManagerDto.getLast_edit_date().getTime());
        }
        adxConfigManagerModel.setLast_edit_user_name(adxConfigManagerDto.getLast_edit_user_name());
        return adxConfigManagerModel;
    }

    protected AdxConfigDto parseAdxConfigModelToDto(AdxConfigModel adxConfigModel) {
        AdxConfigDto adxConfigDto = new AdxConfigDto();
        adxConfigDto.setAdx_id(adxConfigModel.getAdx_id());
        adxConfigDto.setChannel_id(adxConfigModel.getChannel_id());
        adxConfigDto.setCopy_app_id(adxConfigModel.getCopy_app_id());
        adxConfigDto.setApply_app_id(adxConfigModel.getApply_app_id());
        adxConfigDto.setAmount(adxConfigModel.getAmount());
        adxConfigDto.setChannel_name(adxConfigModel.getChannel_name());
        adxConfigDto.setCopy_app_name(adxConfigModel.getCopy_app_name());
        adxConfigDto.setApply_app_name(adxConfigModel.getApply_app_name());
        adxConfigDto.setLast_edit_user_id(adxConfigModel.getLast_edit_user_id());
        adxConfigDto.setLast_edit_user_name(adxConfigModel.getLast_edit_user_name());
        return adxConfigDto;
    }


    protected CustomerConfigModel parseCustomerConfigDtoToModel(CustomerConfigDto customerConfigDto) {
        CustomerConfigModel channelConfigModel = new CustomerConfigModel();
        channelConfigModel.setCustomer_name(customerConfigDto.getCustomer_name());
        channelConfigModel.setCustomer_id(customerConfigDto.getCustomer_id());
        channelConfigModel.setCustomer_describe(customerConfigDto.getCustomer_describe());
        if (null != customerConfigDto.getLast_edit_date()) {
            channelConfigModel.setLast_edit_date(customerConfigDto.getLast_edit_date().getTime());
        }
        channelConfigModel.setLast_edit_user_id(customerConfigDto.getLast_edit_user_id());
        channelConfigModel.setLast_edit_user_name(customerConfigDto.getLast_edit_user_name());
        return channelConfigModel;
    }


    protected CustomerConfigDto parseCustomerConfigModelToDto(CustomerConfigModel customerConfigModel) {
        CustomerConfigDto customerConfigDto = new CustomerConfigDto();
        customerConfigDto.setCustomer_id(customerConfigModel.getCustomer_id());
        customerConfigDto.setCustomer_name(customerConfigModel.getCustomer_name());
        customerConfigDto.setCustomer_describe(customerConfigModel.getCustomer_describe());
        customerConfigDto.setLast_edit_user_id(customerConfigModel.getLast_edit_user_id());
        customerConfigDto.setLast_edit_user_name(customerConfigModel.getLast_edit_user_name());
        return customerConfigDto;
    }


    protected SystemConfigModel parseSystemConfigDtoToModel(SystemConfigDto systemConfigDto) {
        SystemConfigModel systemConfigModel = new SystemConfigModel();
        systemConfigModel.setSystem_id(systemConfigDto.getSystem_id());
        systemConfigModel.setSystem_name(systemConfigDto.getSystem_name());
        systemConfigModel.setCallback_url_base(systemConfigDto.getCallback_url_base());
        if (null != systemConfigDto.getLast_edit_date()) {
            systemConfigModel.setLast_edit_date(systemConfigDto.getLast_edit_date().getTime());
        }
        systemConfigModel.setLast_edit_user_id(systemConfigDto.getLast_edit_user_id());
        systemConfigModel.setLast_edit_user_name(systemConfigDto.getLast_edit_user_name());
        return systemConfigModel;
    }


    protected SystemConfigDto parseSystemConfigModelToDto(SystemConfigModel systemConfigModel) {
        SystemConfigDto systemConfigDto = new SystemConfigDto();
        systemConfigDto.setSystem_id(systemConfigModel.getSystem_id());
        systemConfigDto.setSystem_name(systemConfigModel.getSystem_name());
        systemConfigDto.setCallback_url_base(systemConfigModel.getCallback_url_base());
        systemConfigDto.setLast_edit_user_id(systemConfigModel.getLast_edit_user_id());
        systemConfigDto.setLast_edit_user_name(systemConfigModel.getLast_edit_user_name());
        return systemConfigDto;
    }


    protected AdvConfigModel parseAdvConfigDtoToModel(AdvConfigDto advConfigDto) {
        AdvConfigModel advConfigModel = new AdvConfigModel();
        advConfigModel.setAdv_id(advConfigDto.getAdv_id());
        advConfigModel.setApp_id(advConfigDto.getApp_id());
        advConfigModel.setChannel_id(advConfigDto.getChannel_id());
        advConfigModel.setAdv_describe(advConfigDto.getAdv_describe());
        advConfigModel.setChannel_name(advConfigDto.getChannel_name());
        advConfigModel.setApp_name(advConfigDto.getApp_name());
        advConfigModel.setCustomer_name(advConfigDto.getCustomer_name());
        if (null != advConfigDto.getLast_edit_date()) {
            advConfigModel.setLast_edit_date(advConfigDto.getLast_edit_date().getTime());
        }
        advConfigModel.setLast_edit_user_id(advConfigDto.getLast_edit_user_id());
        advConfigModel.setLast_edit_user_name(advConfigDto.getLast_edit_user_name());

        return advConfigModel;
    }

    protected AdvConfigDto parseAdvConfigModelToDto(AdvConfigModel advConfigModel) {
        AdvConfigDto advConfigDto = new AdvConfigDto();
        advConfigDto.setAdv_id(advConfigModel.getAdv_id());
        advConfigDto.setApp_id(advConfigModel.getApp_id());
        advConfigDto.setAdv_describe(advConfigModel.getAdv_describe());
        advConfigDto.setChannel_id(advConfigModel.getChannel_id());
        advConfigDto.setLast_edit_user_id(advConfigModel.getLast_edit_user_id());
        return advConfigDto;
    }

    protected UserDto parseUserModelToDto(UserModel userModel) {
        UserDto userDto = new UserDto();
        userDto.setUser_id(userModel.getUser_id());
        userDto.setUser_name(userModel.getUser_name());
        userDto.setPassword(userModel.getPassword());
        return userDto;
    }

    protected UserModel parseUserDtoToModel(UserDto userDto) {
        UserModel userModel = new UserModel();
        userModel.setUser_id(userDto.getUser_id());
        userModel.setUser_name(userDto.getUser_name());
        userModel.setIs_administrator(userDto.getIs_administrator() == 1 ? true : false);
        userModel.setIs_approve(userDto.getApprove() == 1 ? true : false);
        return userModel;
    }
}
