package com.advdsp.service.send.memory;

import com.advdsp.service.send.ServiceContextFactory;
import com.advdsp.service.send.db.dto.AdxConfigDto;
import com.advdsp.service.send.service.AdxConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class AdxConfigInfo {
    private static Logger logger = LoggerFactory.getLogger(AdxConfigInfo.class.getName());

    private AdxConfigService adxConfigService = ServiceContextFactory.getAdxConfigService();

    private static AdxConfigInfo adxConfigInfo = new AdxConfigInfo();

    /**
     * key = {channel_id}_${copy_app_id}
     */
    private Map<String, List<AdxConfigDto>> adxMapping = new HashMap<>();

    public static AdxConfigInfo getAdxConfigInfoInstance() {
        return adxConfigInfo;
    }

    public AdxConfigInfo() {
        // init
        loadAdxConfig();
    }

    public void loadAdxConfig() {
        List<AdxConfigDto> adxConfigDtos = adxConfigService.getAll();
        if (null != adxConfigDtos) {
            Map<String, List<AdxConfigDto>> new_adxMapping = new HashMap<>();
            for (AdxConfigDto adxConfigDto : adxConfigDtos) {
                String key = generateKey(adxConfigDto.getChannel_id(), adxConfigDto.getCopy_app_id());
                List<AdxConfigDto> adxConfigDtoList = new_adxMapping.get(key);
                // 第一次插入，构建list
                if (null == adxConfigDtoList) {
                    List<AdxConfigDto> new_adxConfigDtoList = new ArrayList<AdxConfigDto>();
                    new_adxConfigDtoList.add(adxConfigDto);
                    new_adxMapping.put(key, new_adxConfigDtoList);
                } else {
                    adxConfigDtoList.add(adxConfigDto);
                }
            }
            adxMapping = new_adxMapping;
            logger.info("[AdxConfigInfo] load new adx config");
        } else {
            logger.error("[AdxConfigInfo] load adx config failed");
        }
    }

    public List<AdxConfigDto> getApplyAdxConfigList(Integer channel_id, Integer copy_app_id) {
        String key = generateKey(channel_id, copy_app_id);
        return adxMapping.get(key);
    }

    private String generateKey(Integer channel_id, Integer copy_app_id) {
        return channel_id + "_" + copy_app_id;
    }
}
