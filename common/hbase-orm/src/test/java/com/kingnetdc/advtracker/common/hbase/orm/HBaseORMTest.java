package com.kingnetdc.advtracker.common.hbase.orm;

import com.kingnetdc.advtracker.common.hbase.orm.cass.jpa.access.HBaseAccess;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.ConnectionFactory;

/**
 * 
 * ORM test class
 * 
 * @author wz68
 *
 */
public class HBaseORMTest {


	public static Configuration conf;

	static {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum","localhost");
		// conf.addResource("hbase-site.xml");
	}

	public static void main(String[] args) throws Exception {

		TestAdvData testAdvData = new TestAdvData();
		testAdvData.setRowkey("test1");
		testAdvData.setIp("127.0.0.2");
		testAdvData.setTimestamp(System.currentTimeMillis());

		HBaseAccess hBaseAccess = new HBaseAccess(ConnectionFactory.createConnection(conf));

		hBaseAccess.save(testAdvData);

		TestAdvData testAdvData1 = hBaseAccess.getByRowkey("test1",TestAdvData.class);
		System.out.println(testAdvData1.getRowkey()+":"+testAdvData1.getIp()+":"+testAdvData1.getTimestamp());

	}
}
