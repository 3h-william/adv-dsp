package com.advdsp.service.dsp.processor.kv;

import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access.HBaseAccess;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by william on 2017/5/23.
 */
public class KVProcessorFactory {

    private static final String nameSpaceConf = "namespace.name";
    private static final String userNameConf = "hbase.user.name";
    private static Logger logger = LoggerFactory.getLogger(KVProcessorFactory.class.getName());
    public static Configuration conf;
    public static Connection connection;

    static {
        try {
            conf = HBaseConfiguration.create();
            conf.addResource("hbase-site.xml");
            logger.info("hbase init config:  \n" +
                    "hbase.zookeeper.quorum=" + conf.get("hbase.zookeeper.quorum") + "\n" +
                    "namespace.name=" + conf.get(nameSpaceConf, "default") + "\n" +
                    "hbase.user.name=" + conf.get(userNameConf)
            );
            if (StringUtils.isNoneEmpty(conf.get(userNameConf))) {
                User uCreated = User.createUserForTesting(conf, conf.get(userNameConf), new String[0]);
                connection = ConnectionFactory.createConnection(conf, uCreated);
            } else {
                connection = ConnectionFactory.createConnection(conf);
            }
        } catch (IOException e) {
            throw new RuntimeException("initialized hbase configuration failed", e);
        }
    }

    private static HBaseAccess getHBaseAccess() {
        if (conf.onlyKeyExists(nameSpaceConf)) {
            return new HBaseAccess(connection);
        } else {
            return new HBaseAccess(connection, conf.get(nameSpaceConf));
        }
    }

    private static KVDataProcessor kvDataProcessor = new HBaseDataProcessorFactory(getHBaseAccess());

    public static KVDataProcessor getInstance() {
        return kvDataProcessor;
    }
}
