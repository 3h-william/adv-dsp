package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.AdvStatisticDto;
import com.advdsp.service.manager.rest.model.statistic.AdvStatisticModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Path("/statistic")
public class StatisticResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(StatisticResources.class.getName());
    private final int formatPrecision =3;
    final FastDateFormat dsDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAll(@QueryParam("channel_id") String channel_id, @QueryParam("app_id") String app_id,
                                    @QueryParam("start_time") Long start_time, @QueryParam("end_time") Long end_time) {
        logger.info("[statistic:search] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            boolean isAppIdSkip = true;
            boolean isChannelIdSkip = true;
            String startDate = "";
            String endDate = "";

            if (StringUtils.isNoneEmpty(app_id)) {
                isAppIdSkip = false;
            }

            if (StringUtils.isNoneEmpty(channel_id)) {
                isChannelIdSkip = false;
            }

            if (StringUtils.isNoneEmpty(app_id)) {
                isAppIdSkip = false;
            }

            if (null == start_time) {
                startDate = "1970-1-1";
            } else {
                startDate = dsDateFormat.format(start_time);
            }

            if (null == end_time) {
                endDate = "2100-1-1";
            } else {
                endDate = dsDateFormat.format(end_time);
            }
            List<AdvStatisticDto> advStatisticDtos = advStatisticService.searchStatistic(isAppIdSkip, app_id, isChannelIdSkip, channel_id, startDate, endDate);
            List<AdvStatisticModel> advStatisticModels = new ArrayList<>();
            if (null != advStatisticDtos) {
                for (AdvStatisticDto advStatisticDto : advStatisticDtos) {
                    advStatisticModels.add(parseAdvStatisticDtoToModel(advStatisticDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(advStatisticModels);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[statistic:search] failed", t);
        }
        return wrapResponseModel;
    }

    private AdvStatisticModel parseAdvStatisticDtoToModel(AdvStatisticDto advStatisticDto) {

        AdvStatisticModel advStatisticModel = new AdvStatisticModel();
        advStatisticModel.setApp_id(advStatisticDto.getApp_id());
        advStatisticModel.setChannel_id(advStatisticDto.getChannel_id());
        advStatisticModel.setClick_nums(advStatisticDto.getClick_nums());
        advStatisticModel.setDistinct_click_nums(advStatisticDto.getDistinct_click_nums());
        advStatisticModel.setDistinct_activation_nums(advStatisticDto.getDistinct_activation_nums());
        advStatisticModel.setActivation_nums(advStatisticDto.getActivation_nums());
        advStatisticModel.setDs(advStatisticDto.getDs());
        advStatisticModel.setApp_name(advStatisticDto.getApp_name());
        advStatisticModel.setChannel_name(advStatisticDto.getChannel_name());
        //set cvda
        advStatisticModel.setCvda(formatPrecisionWithPercentage(formatPrecision,
                Double.valueOf(advStatisticModel.getDistinct_activation_nums()) / Double.valueOf(advStatisticModel.getClick_nums())));
        return advStatisticModel;
    }

    private String formatPrecisionWithPercentage(Integer precision, Double value) {
        if (value == Double.POSITIVE_INFINITY) {
            return "无点击";
        } else {
            return String.format("%." + precision + "f", value * 100).toString() + "%";
        }
    }
}
