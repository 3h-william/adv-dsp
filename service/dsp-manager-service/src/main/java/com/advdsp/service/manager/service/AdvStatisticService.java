package com.advdsp.service.manager.service;

import com.advdsp.service.manager.db.dao.AdvConfigDao;
import com.advdsp.service.manager.db.dao.AdvStatisticDao;
import com.advdsp.service.manager.db.dto.AdvConfigDto;
import com.advdsp.service.manager.db.dto.AdvStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class AdvStatisticService {
    @Autowired
    private AdvStatisticDao advStatisticDao;

    public void saveObjects(List<AdvStatisticDto> advStatisticDtos) {
        advStatisticDao.saveObjects(advStatisticDtos);
    }

    public List<AdvStatisticDto> searchStatistic(boolean isAppIdSkip, String app_id,
                                                 boolean isChannelIdSkip, String channel_id, String startDate, String endDate) {
        return advStatisticDao.searchStatistic(isAppIdSkip, app_id, isChannelIdSkip, channel_id, startDate, endDate);
    }


}
