package com.lv.redis.rediscombat.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 吕明亮
 * @Date : 2019/9/3 09:35
 * @Description:
 */

public class JedisUtilTest {
    @Test
    public void getJedis() throws Exception {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.ping();
    }

    @Test
    public void ping() throws Exception {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.ping();
    }

    @Test
    public void close() throws Exception {

    }

    @Test
    public void poolPing() throws Exception {
    }

    @Test
    public void poolPings() throws Exception {
    }

    @Test
    public void del() throws Exception {
    }

    @Test
    public void dump() throws Exception {
    }

    @Test
    public void exists() throws Exception {
    }

    @Test
    public void expire() throws Exception {
    }

    @Test
    public void expireat() throws Exception {
        JedisUtil jedisUtil = new JedisUtil();
        String key = "name";
        Long time = System.currentTimeMillis();
        jedisUtil.expireAt(key, time);
    }

    @Test
    public void pexpire() throws Exception {
    }

    @Test
    public void pexpireAt() throws Exception {
    }

    @Test
    public void keys() throws Exception {
    }

    @Test
    public void move() throws Exception {
    }

    @Test
    public void ttl() throws Exception {
    }

    @Test
    public void pttl() throws Exception {
    }

    @Test
    public void randomKey() throws Exception {
    }

    @Test
    public void reName() throws Exception {
    }

    @Test
    public void reNameNx() throws Exception {
    }

    @Test
    public void type() throws Exception {
    }


    @Test
    public void testBlpop() {
        JedisUtil jedisUtil = new JedisUtil();

        List<String> result = jedisUtil.blpop(1, "class");

        result.stream().forEach(e->{
            System.out.println(e);
        });

    }

}