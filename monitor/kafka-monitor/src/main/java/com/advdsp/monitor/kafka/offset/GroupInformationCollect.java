package com.advdsp.monitor.kafka.offset;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class GroupInformationCollect {

    /**
     * key = topic name
     */
    private Map<String, GroupInformationStatistic> statisticMap = new HashMap();

    public void collectPartitionInfo(GroupInformation groupInformation) {

        GroupInformationStatistic groupInformationStatistic = statisticMap.get(groupInformation.getTopic_name());
        if (null == groupInformationStatistic) {
            groupInformationStatistic = new GroupInformationStatistic();
            groupInformationStatistic.setGroup_name(groupInformation.getGroup_name());
            groupInformationStatistic.setTopic_name(groupInformation.getTopic_name());
            statisticMap.put(groupInformationStatistic.getTopic_name(), groupInformationStatistic);
        }

        Long lag = groupInformation.getLag();
        groupInformationStatistic.setLag_total(groupInformationStatistic.getLag_total() + lag);
        groupInformationStatistic.setPartition_nums(groupInformationStatistic.getPartition_nums() + 1);

        if (groupInformationStatistic.getLag_max() < lag) {
            groupInformationStatistic.setLag_max(lag);
        }

        groupInformationStatistic.setLag_mean(groupInformationStatistic.getLag_total() / groupInformationStatistic.getPartition_nums());
    }


    public GroupInformationStatistic getStatistic(String groupName) {
        return statisticMap.get(groupName);
    }

}
