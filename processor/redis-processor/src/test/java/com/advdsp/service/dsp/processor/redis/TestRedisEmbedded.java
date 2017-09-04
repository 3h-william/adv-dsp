package com.advdsp.service.dsp.processor.redis;

import redis.embedded.RedisServer;

import java.io.IOException;

/**
 */
public class TestRedisEmbedded {
    public static void main(String[] args) throws IOException {
        RedisServer redisServer = RedisServer.builder()
                //.configFile()
                .port(6379)
                .slaveOf("locahost", 6378)
                .setting("daemonize no")
                .setting("appendonly no")
                //.setting("maxheap 128M")
                .build();
        redisServer.start();
    }
}
