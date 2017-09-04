package com.advdsp.service.manager.statistic;

import com.advdsp.service.dsp.model.CounterDataModel;
import com.advdsp.service.dsp.processor.kv.KVProcessorFactory;
import com.advdsp.service.manager.ServiceConfiguration;
import com.advdsp.service.manager.ServiceContextFactory;
import com.advdsp.service.manager.db.dto.AdvStatisticDto;
import com.advdsp.service.manager.service.AdvStatisticService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 */
public class StatisticLoader implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(StatisticLoader.class.getName());

    private final Integer reload_interval = ServiceConfiguration.getInstance().getConfig().getInt("dsp.manager.statistic.reload.interval", 10);
    private final Integer date_range = ServiceConfiguration.getInstance().getConfig().getInt("dsp.manager.statistic.reload.date.range", 7);

    AdvStatisticService advStatisticService = ServiceContextFactory.advStatisticService;

    @Override
    public void run() {
        logger.info("[StatisticLoader] start backend thread ");
        while (true) {
            try {
                logger.info("[StatisticLoader] load new statistic data");
                //计算起始时间
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -date_range);
                List<CounterDataModel> counterDataModelList = KVProcessorFactory.getInstance().getAllCounters(calendar.getTimeInMillis(), null);
                if (null != counterDataModelList) {
                    List<AdvStatisticDto> advStatisticDtos = new ArrayList<>();
                    for (CounterDataModel counterDataModel : counterDataModelList) {
                        //check
                        if (StringUtils.isNumeric(counterDataModel.getChannel_id()) && StringUtils.isNumeric(counterDataModel.getApp_id())) {
                            AdvStatisticDto advStatisticDto = new AdvStatisticDto();
                            advStatisticDto.setClick_nums(counterDataModel.getClick_nums());
                            advStatisticDto.setDistinct_click_nums(counterDataModel.getDistinct_click_nums());
                            advStatisticDto.setActivation_nums(counterDataModel.getActivation_nums());
                            advStatisticDto.setDistinct_activation_nums(counterDataModel.getDistinct_activation_nums());
                            advStatisticDto.setApp_id(counterDataModel.getApp_id());
                            advStatisticDto.setChannel_id(counterDataModel.getChannel_id());
                            advStatisticDto.setDs(counterDataModel.getDs());
                            advStatisticDtos.add(advStatisticDto);
                        }
                    }
                    if (0 != advStatisticDtos.size()) {
                        advStatisticService.saveObjects(advStatisticDtos);
                    }
                }
            } catch (Throwable t) {
                logger.error("[StatisticLoader] failed ", t);
            }
            try {
                Thread.sleep(reload_interval * 1000);
            } catch (InterruptedException e) {
                logger.error("[StatisticLoader] sleep error ", e);
            }
        }
    }
}
