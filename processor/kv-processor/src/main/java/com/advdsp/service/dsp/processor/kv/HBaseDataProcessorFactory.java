package com.advdsp.service.dsp.processor.kv;

import com.advdsp.service.dsp.common.type.KeyUtils;
import com.advdsp.service.dsp.model.ActionDataModel;
import com.advdsp.service.dsp.model.CounterDataModel;
import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access.HBaseAccess;
import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access.HBaseOperate;
import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access.HBaseOperateWithReturn;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class HBaseDataProcessorFactory implements KVDataProcessor {
    protected final HBaseAccess hBaseAccess;

    public HBaseDataProcessorFactory(HBaseAccess hBaseAccess) {
        this.hBaseAccess = hBaseAccess;
    }

    /**
     * 保留7天 ds_data数据
     */
    private final Long DS_DATA_TTL = 7 * 24 * 60 * 60 * 1000L;

    private final String COUNTER_TABLE = "counter";
    private final String COUNTER_BASE_INFO = "base_info";

    /**
     * exist = true  ; else = false
     *
     * @param actionDataModel
     * @return
     * @throws Throwable
     */
    @Override
    public boolean isActionDataExist(ActionDataModel actionDataModel) throws Throwable {
//        ActionDataModel existActionDataModel = hBaseAccess.getByRowkey(KeyUtils.generateKeyWithNoTs(actionDataModel), ActionDataModel.class);
//        return null != existActionDataModel;
        return hBaseAccess.isKeyExist(KeyUtils.generateKeyWithNoTs(actionDataModel), ActionDataModel.class);
    }

    @Override
    public boolean isActionDataWithTsExist(ActionDataModel actionDataModel) throws Throwable {
        throw new RuntimeException("not support");
    }

    @Override
    public void saveActionData(ActionDataModel actionDataModel) throws Throwable {
        actionDataModel.setRowkey(KeyUtils.generateKeyWithNoTs(actionDataModel));
        hBaseAccess.save(actionDataModel);
    }

    @Override
    public void saveActionDataWithTTL(ActionDataModel actionDataModel) throws Throwable {
        actionDataModel.setRowkey(KeyUtils.generateKeyWithNoTs(actionDataModel));
        hBaseAccess.saveWithTTL(actionDataModel, DS_DATA_TTL);
    }

    @Override
    public void saveActionDataWithTs(ActionDataModel actionDataModel) throws Throwable {

    }

    @Override
    public boolean isClickActionDataWithDs(ActionDataModel actionDataModel) throws Throwable {
        if (StringUtils.isEmpty(actionDataModel.getChannel())) {
            throw new RuntimeException("click channel is empty");
        }
        return hBaseAccess.isKeyExist(KeyUtils.generateClickKeyWithDs(actionDataModel), ActionDataModel.class);
    }

    @Override
    public void saveClickActionDataWithDs(ActionDataModel actionDataModel) throws Throwable {
        actionDataModel.setRowkey(KeyUtils.generateClickKeyWithDs(actionDataModel));
        hBaseAccess.saveWithTTL(actionDataModel, DS_DATA_TTL);
    }

    @Override
    public ActionDataModel getActionData(String key) throws Throwable {
        return hBaseAccess.getByRowkey(key, ActionDataModel.class);
    }

    @Override
    public ActionDataModel getActionDataWithTs(String key) throws Throwable {
        throw new RuntimeException("not support");
    }

    @Override
    public void addCounter(String counterKey, String indicatorValue) throws Exception {
        Table table = hBaseAccess.getTable(COUNTER_TABLE);
        HBaseOperate inc = (htable) -> {
            Increment increment = new Increment(Bytes.toBytes(counterKey));
            increment.addColumn(Bytes.toBytes(COUNTER_BASE_INFO), Bytes.toBytes(indicatorValue), 1);
            htable.increment(increment);
        };
        hBaseAccess.operate(inc, table);
    }

    @Override
    public List<CounterDataModel> getAllCounters(Long startTime, Long endTime) throws Throwable {
        Table table = hBaseAccess.getTable(COUNTER_TABLE);
        HBaseOperateWithReturn scanCounter = (htable) -> {
            SimpleDateFormat dsFormat = new SimpleDateFormat("yyyy-MM-dd");
            Scan scan = new Scan();
            if (null != startTime) {
                String start_ds = dsFormat.format(startTime);
                scan.setStartRow(Bytes.toBytes(start_ds));
            }
            if (null != endTime) {
                String end_ds = dsFormat.format(endTime);
                scan.setStartRow(Bytes.toBytes(end_ds));
            }
            // 只获取一个， 优化,可以获取多个step ，如果考虑到用UA等去匹配
            scan.setMaxResultSize(1);
            ResultScanner resultScanner = htable.getScanner(scan);
            Result result = null;
            List<CounterDataModel> counterDataModelList = new ArrayList<>();
            while (null != (result = resultScanner.next())) {
                counterDataModelList.add(hBaseAccess.getBean(result, CounterDataModel.class));
            }
            return counterDataModelList;
        };
        //填充 channelid & appid
        List<CounterDataModel> counterDataModelList = hBaseAccess.operateWithReturn(scanCounter, table);
        for (CounterDataModel counterDataModel : counterDataModelList) {
            // correct format  {ds}_{appID}_{channel_id}
            String[] splits = counterDataModel.getRowkey().split("_");
            counterDataModel.setDs(splits[0]);
            counterDataModel.setApp_id(splits[1]);
            counterDataModel.setChannel_id(splits[2]);
        }
        return counterDataModelList;
    }

    @Override
    public void mergeCounters(String rowkey) throws Throwable {
        CounterDataModel old_counterDataModel = hBaseAccess.getByRowkey(rowkey, CounterDataModel.class);
        // wrong format   {appID}_{channel_id}_{ds}
        String[] splits = rowkey.split("_");
        old_counterDataModel.setApp_id(splits[0]);
        old_counterDataModel.setChannel_id(splits[1]);
        old_counterDataModel.setDs(splits[2]);

        // correct format  {ds}_{appID}_{channel_id}
        String newRowkey = old_counterDataModel.getDs() + "_" + old_counterDataModel.getApp_id() + "_" + old_counterDataModel.getChannel_id();
        CounterDataModel new_counterDataModel = hBaseAccess.getByRowkey(newRowkey, CounterDataModel.class);
        if (null == new_counterDataModel) {
            new_counterDataModel = new CounterDataModel();
            new_counterDataModel.setRowkey(newRowkey);
        }
        new_counterDataModel.setClick_nums(old_counterDataModel.getClick_nums() + new_counterDataModel.getClick_nums());
        new_counterDataModel.setDistinct_click_nums(old_counterDataModel.getDistinct_click_nums() + new_counterDataModel.getDistinct_click_nums());
        new_counterDataModel.setActivation_nums(old_counterDataModel.getActivation_nums() + new_counterDataModel.getActivation_nums());
        new_counterDataModel.setDistinct_activation_nums(old_counterDataModel.getDistinct_activation_nums() + new_counterDataModel.getDistinct_activation_nums());

        hBaseAccess.save(new_counterDataModel);

    }


}
