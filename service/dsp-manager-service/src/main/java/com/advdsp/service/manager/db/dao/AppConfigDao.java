package com.advdsp.service.manager.db.dao;

import com.advdsp.service.manager.db.dto.AppConfigDto;

import java.util.List;

/**
 */
public interface AppConfigDao extends BaseDao {

    List<AppConfigDto> getNoAdxConfig(Integer channel_id, Integer app_id);
}

