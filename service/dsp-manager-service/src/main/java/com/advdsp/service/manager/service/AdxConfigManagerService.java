package com.advdsp.service.manager.service;

import com.advdsp.service.manager.db.dao.AdxConfigManagerDao;
import com.advdsp.service.manager.db.dto.AdxConfigDto;
import com.advdsp.service.manager.db.dto.AppChannelMappingDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class AdxConfigManagerService {
    @Autowired
    private AdxConfigManagerDao adxConfigManagerDao;

    /**
     * 搜索
     *
     * @param channel_id
     * @param copy_app_id
     * @return
     */
    public List<AdxConfigDto> searchAdxConfigMapping(String channel_id, String copy_app_id) {
        return adxConfigManagerDao.searchAdxConfigMapping(channel_id, copy_app_id);
    }

    /**
     * 根据channel_id, copy_app_id 搜索
     *
     * @param channel_id
     * @param copy_app_id
     * @return
     */
    public List<AppChannelMappingDto> searchAppConfigMapping(String channel_id, String copy_app_id) {
        boolean isChannelIdSkip = StringUtils.isEmpty(channel_id) ? true : false;
        boolean isCopyAppIdSkip = StringUtils.isEmpty(copy_app_id) ? true : false;
        return adxConfigManagerDao.searchAppConfigMapping(isChannelIdSkip, channel_id, isCopyAppIdSkip, copy_app_id);
    }

    public void saveObject(AdxConfigDto adxConfigDto) {
        adxConfigManagerDao.saveObject(adxConfigDto);
    }

    public void updateObjectById(AdxConfigDto adxConfigDto) {
        adxConfigManagerDao.updateObjectById(adxConfigDto);
    }

    public void deleteById(Integer adx_id) {
        adxConfigManagerDao.deleteById(adx_id);
    }

    public void deleteByChannelIdAndCopyAppId(Integer channel_id, Integer copy_app_id) {
        adxConfigManagerDao.deleteByChannelIdAndCopyAppId(channel_id, copy_app_id);
    }

}
