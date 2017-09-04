package com.advdsp.service.manager.db.dao;

import com.advdsp.service.manager.db.dto.AdvStatisticDto;

import java.util.List;

/**
 */
public interface AdvStatisticDao extends BaseDao {

    List<AdvStatisticDto> searchStatistic(boolean isAppIdSkip, String app_id, boolean isChannelIdSkip, String channel_id, String startDate, String endDate);
}
