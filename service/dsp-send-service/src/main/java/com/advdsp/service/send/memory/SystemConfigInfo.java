package com.advdsp.service.send.memory;

import com.advdsp.service.send.ServiceContextFactory;
import com.advdsp.service.send.db.dto.SystemConfigDto;
import com.advdsp.service.send.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class SystemConfigInfo {
    private static Logger logger = LoggerFactory.getLogger(SystemConfigInfo.class.getName());

    private SystemConfigService systemConfigService = ServiceContextFactory.getSystemConfigService();
    private static SystemConfigInfo systemConfigInfo = new SystemConfigInfo();

    private Map<Integer, SystemConfigDto> systemConfigInfoMap = new HashMap<>();

    public static SystemConfigInfo getSystemConfigInfoInstance() {
        return systemConfigInfo;
    }

    public SystemConfigInfo() {
        // init
        loadSystemConfig();
    }

    public void loadSystemConfig() {
        List<SystemConfigDto> systemConfigDtos = systemConfigService.getAll();
        if (null != systemConfigDtos) {
            Map<Integer, SystemConfigDto> new_systemConfigInfoMap = new HashMap<>();
            for (SystemConfigDto systemConfigDto : systemConfigDtos) {
                new_systemConfigInfoMap.put(systemConfigDto.getSystem_id(), systemConfigDto);
            }
            systemConfigInfoMap = new_systemConfigInfoMap;
            logger.info("[SystemConfigInfo] load new system config");
        } else {
            logger.error("[SystemConfigInfo] load system config failed");
        }
    }

    public String getSystemCallBackUrlBase(Integer systemId) {
        SystemConfigDto systemConfigDto = systemConfigInfoMap.get(systemId);
        if (null == systemConfigDto) {
            logger.warn("[SystemConfigInfo] system not found ,systemId = " + systemId);
            return null;
        } else {
            return systemConfigDto.getCallback_url_base();
        }
    }
}
