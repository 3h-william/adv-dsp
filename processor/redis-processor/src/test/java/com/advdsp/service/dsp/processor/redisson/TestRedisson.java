package com.advdsp.service.dsp.processor.redisson;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by william on 2017/7/10.
 */
public class TestRedisson {
    public static void main(String[] args) throws IOException {
        //Config config = Config.fromJSON(getFileInputStream("redisson-client.json"));
        Config config = new Config();
        //config.setUseLinuxNativeEpoll(true);
//        config.useClusterServers()
//                // use "rediss://" for SSL connect
//                // ion
//                .addNodeAddress("redis://127.0.0.1:6379");

        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
                // use "rediss://" for SSL connection

        System.out.println(config.toJSON());

        RedissonClient client = Redisson.create(config);

        RAtomicLong longObject = client.getAtomicLong("myLong");
        longObject.addAndGet(1);
        client.shutdown();
    }

    public static InputStream getFileInputStream(String fileName){
        return  TestRedisson.class.getClassLoader().getResourceAsStream(fileName);
    }
}

