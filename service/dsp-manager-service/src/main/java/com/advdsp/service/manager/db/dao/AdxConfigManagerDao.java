package com.advdsp.service.manager.db.dao;

import com.advdsp.service.manager.db.dto.AdxConfigDto;
import com.advdsp.service.manager.db.dto.AppChannelMappingDto;

import java.util.List;

/**
 */
public interface AdxConfigManagerDao extends BaseDao {

    List<AdxConfigDto> searchAdxConfigMapping(String channel_id, String copy_app_id);

    List<AppChannelMappingDto> searchAppConfigMapping(boolean isChannelIdSkip, String channel_id, boolean isCopyAppIdSkip, String copy_app_id);

    void deleteByChannelIdAndCopyAppId(Integer channel_id, Integer copy_app_id);

}
