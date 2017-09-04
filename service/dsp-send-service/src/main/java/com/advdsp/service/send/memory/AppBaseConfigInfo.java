package com.advdsp.service.send.memory;

import com.advdsp.service.dsp.common.exception.AdvDspException;
import com.advdsp.service.send.ServiceContextFactory;
import com.advdsp.service.send.db.dto.AppBaseConfigDto;
import com.advdsp.service.send.service.AppBaseConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class AppBaseConfigInfo {
    private static Logger logger = LoggerFactory.getLogger(AppBaseConfigInfo.class.getName());

    private AppBaseConfigService appConfigService = ServiceContextFactory.getAppConfigService();

    private static AppBaseConfigInfo appConfigInfo = new AppBaseConfigInfo();

    private Map<String, AppBaseConfigDto> appBaseConfigDtoMap = new HashMap<>();

    public static AppBaseConfigInfo getAppConfigInfoInstance() {
        return appConfigInfo;
    }

    public AppBaseConfigInfo() {
        // init
        loadAppConfig();
    }

    public void loadAppConfig() {
        List<AppBaseConfigDto> appBaseConfigs = appConfigService.getAll();
        if (null != appBaseConfigs) {
            Map<String, AppBaseConfigDto> new_appBaseConfigDtoMap = new HashMap<>();
            for (AppBaseConfigDto appBaseConfigDto : appBaseConfigs) {
                new_appBaseConfigDtoMap.put(appBaseConfigDto.getApp_id(), appBaseConfigDto);
            }
            appBaseConfigDtoMap = new_appBaseConfigDtoMap;
            logger.info("[AppBaseConfigInfo] load new app config");
        } else {
            logger.error("[AppBaseConfigInfo] load app config failed");
        }
    }

    public String getAppCallBackUrlBase(String app_id) {
        AppBaseConfigDto appBaseConfigDto = appBaseConfigDtoMap.get(app_id);
        if (null == appBaseConfigDto) {
            logger.warn("[AppBaseConfigInfo] appid not found ,app_id = " + app_id);
            return null;
        } else {
            return appBaseConfigDto.getCallback_url_base();
        }
    }

    public AppBaseConfigDto getAppBaseConfig(String app_id) throws AdvDspException {
        AppBaseConfigDto appConfigDto = appBaseConfigDtoMap.get(app_id);
        if (null == appConfigDto) {
            throw new AdvDspException("[appBaseConfigDto] appid not found ,app_id = " + app_id);
        } else {
            return appConfigDto;
        }
    }
}
