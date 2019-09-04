package com.lv.redis.rediscombat.util;


import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 吕明亮
 * @Date : 2019/9/2 16:16
 * @Description:
 */
@Component
public class JedisUtil {

    /**
     * jedis连接池
     */
    public static JedisPool pool = null;

    static {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(5);
            config.setMaxWaitMillis(100);
            config.setTestOnBorrow(true);
            //pool = new JedisPool(config, "127.0.0.1", 6379, 100000, "");
            pool = new JedisPool(config, "127.0.0.1", 6379, 100000);
        }
    }

    public String ip = "127.0.0.1";
    private int port = 6379;
    private String auth = "";

    public static Jedis jedis;

    /**
     * 直接获取jedis
     *
     * @param host
     * @param port
     * @return
     */
    public static Jedis getJedis(String host, int port) {
        if (jedis == null) {
            jedis = new Jedis(host, port);
        }
        return jedis;
    }

    public JedisUtil() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(5);
            config.setMaxWaitMillis(100);
            config.setTestOnBorrow(true);
            //pool = new JedisPool(config, this.ip, this.port, 100000, this.auth);
            pool = new JedisPool(config, this.ip, this.port, 100000);
        }
    }


    public void ping() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            System.out.println(jedis.ping());
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
    }

    /**
     * jedisPool 废弃了归还连接 returnBrokenResource ，直接jedis.close() 即可
     *
     * @param jedis
     */
    public void close(Jedis jedis) {
        if (jedis != null) {
            //System.out.println("关闭连接：" + jedis);
            jedis.close();
        }
    }


    public void poolPing() {
        JedisPool jedisPool = new JedisPool();
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.ping());
    }


    public void poolPings() {
        Jedis jedis = pool.getResource();
        System.out.println(jedis.ping());
    }


    /*************  key 的基本操作**************/


    /**
     * 用于删除已存在的键。不存在的 key 会被忽略
     *
     * @param key
     * @return
     */
    public Long del(String... key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
            return 0L;
        } finally {
            close(jedis);
        }
    }

    /**
     * 用于序列化给定 key ，并返回被序列化的值。
     *
     * @param key
     * @return byte[] 序列号的byte数组
     */
    public byte[] dump(String key) {

        Jedis jedis = null;
        byte[] result = null;
        try {
            jedis = pool.getResource();
            result = jedis.dump(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }


    /**
     * 用于检查给定 key 是否存在
     *
     * @param key
     * @return boolean(存在返回true, 不存在返回false)
     */
    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
            return false;
        } finally {
            close(jedis);
        }
    }


    /**
     * 设置 key 的过期时间，key 过期后将不再可用。单位以秒计
     *
     * @param key
     * @param times
     */
    public void expire(String key, int times) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.expire(key, times);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
    }

    /**
     * 用于以UNIX 时间戳(unix timestamp)格式设置 key 的过期时间。key 过期后将不再可用
     *
     * @param key
     * @param unixTime
     */
    public void expireAt(String key, long unixTime) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.expireAt(key, unixTime);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
    }

    /**
     * 以毫秒为单位设置 key 的生存时间，而不像 EXPIRE 命令那样，以秒为单位
     *
     * @param key
     * @param milliseconds
     */
    public void pexpire(String key, long milliseconds) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.pexpire(key, milliseconds);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
    }

    /**
     * 用于设置 key 的过期时间，以毫秒计。key 过期后将不再可用
     *
     * @param key
     * @param milliseconds
     */
    public void pexpireAt(String key, long milliseconds) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.pexpireAt(key, milliseconds);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
    }

    /**
     * 用于查找所有符合给定模式 pattern 的 key 。。
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.keys(pattern);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 用于将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key
     * @param dbIndex
     * @return
     */
    public Long move(String key, int dbIndex) {

        Jedis jedis = null;
        Long total = 0L;
        try {
            jedis = pool.getResource();
            total = jedis.move(key, dbIndex);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return total;
    }

    /**
     * 以秒为单位查看过期时间
     *
     * @param key
     * @return
     */
    public Long ttl(String key) {
        Jedis jedis = null;
        Long passDueTime = 0L;
        try {
            jedis = pool.getResource();
            passDueTime = jedis.ttl(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return passDueTime;
    }


    /**
     * 以毫秒为单位返回 key 的剩余过期时间
     *
     * @param key
     * @return
     */
    public Long pttl(String key) {
        Jedis jedis = null;
        Long passDueTime = 0L;
        try {
            jedis = pool.getResource();
            passDueTime = jedis.pttl(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return passDueTime;
    }

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return
     */
    public String randomKey() {
        Jedis jedis = null;
        String randomKey = null;
        try {
            jedis = pool.getResource();
            randomKey = jedis.randomKey();
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return randomKey;
    }

    /**
     * 用于修改 key 的名称
     *
     * @param oldKeyName
     * @param newKeyName
     * @return 改名成功时提示 OK ，失败时候返回一个错误   ,key 不存在时返回 ERR no such key
     * 当 OLD_KEY_NAME 和 NEW_KEY_NAME 相同，或者 OLD_KEY_NAME 不存在时，返回一个错误。 当 NEW_KEY_NAME 已经存在时， RENAME 命令将覆盖旧值
     */
    public String reName(String oldKeyName, String newKeyName) {

        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.rename(oldKeyName, newKeyName);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * 新的 key 不存在时修改 key 的名称
     *
     * @param oldKeyName
     * @param newKeyName
     * @return 修改成功时，返回 1 。 如果 NEW_KEY_NAME 已经存在，返回 0
     */
    public Long reNameNx(String oldKeyName, String newKeyName) {

        Jedis jedis = null;
        Long result = null;
        try {
            jedis = pool.getResource();
            result = jedis.renamenx(oldKeyName, newKeyName);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }


    /**
     * 返回 key 所储存的值的类型
     *
     * @param key
     * @return
     */
    public String type(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.type(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }

/*****************************************  Redis 字符串(String) ************************************************/

    /**
     * SET 命令用于设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * 用于获取指定 key 的值。如果 key 不存在，返回 nil 。如果key 储存的值不是字符串类型，返回一个错误
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return value;
    }

    /**
     * 获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)
     *
     * @param key
     * @param startOffset
     * @param endOffset
     * @return 如果没有返回null
     */
    public String getRange(String key, int startOffset, int endOffset) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            value = jedis.getrange(key, startOffset, endOffset);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return value;
    }


    /**
     * 用于设置指定 key 的值，并返回 key 的旧值
     *
     * @param key
     * @param value
     * @return
     */
    public String getSet(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * 返回所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值  null
     *
     * @param keys
     * @return
     */
    public List<String> mget(String... keys) {
        Jedis jedis = null;
        List<String> values = null;
        try {
            jedis = pool.getResource();
            values = jedis.mget(keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return values;
    }


    /**
     * 指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值
     *
     * @param key
     * @param value
     * @param seconds 单位:
     * @return 成功返回OK 失败和异常返回null
     */
    public String setEx(String key, String value, int seconds) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 在指定的 key 不存在时，为 key 设置指定的值
     *
     * @param key
     * @param value
     * @return
     */
    public Long setNx(String key, String value) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.setnx(key, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始
     *
     * @param key
     * @param value
     * @param offset
     * @return
     */
    public Long setRange(String key, String value, int offset) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.setrange(key, offset, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 获取指定 key 所储存的字符串值的长度。当 key 储存的不是字符串值时，返回一个错误
     *
     * @param key
     * @return
     */
    public Long strLen(String key) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.strlen(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 用于同时设置一个或多个 key-value 对
     *
     * @param keysValues
     * @return
     */
    public String mSet(String... keysValues) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.mset(keysValues);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 当所有 key 都成功设置，返回 1 。 如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0
     *
     * @param keysValues
     * @return
     */
    public Long mSetNx(String... keysValues) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.msetnx(keysValues);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 以毫秒为单位设置 key 的生存时间
     *
     * @param key
     * @param milliseconds
     * @param value
     * @return
     */
    public String pSetEx(String key, Long milliseconds, String value) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.psetex(key, milliseconds, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 将 key 中储存的数字值增一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.incr(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 将 key 中储存的数字加上指定的增量值。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(String key, Long integer) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.incrBy(key, integer);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 为 key 中所储存的值加上指定的浮点数增量值。
     * 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作
     *
     * @param key
     * @param value
     * @return
     */
    public Double incrByFloat(String key, Double value) {
        Jedis jedis = null;
        Double res = null;
        try {
            jedis = pool.getResource();
            res = jedis.incrByFloat(key, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 将 key 中储存的数字值减一。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @return
     */
    public Long decr(String key) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.decr(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 将 key 所储存的值减去指定的减量值。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     *
     * @param key
     * @param decrment
     * @return
     */
    public Long decrBy(String key, Long decrment) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.decrBy(key, decrment);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 用于为指定的 key 追加值。
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     *
     * @param key
     * @param value
     * @return
     */
    public Long append(String key, String value) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.append(key, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /****************************** Redis 哈希(Hash) *********************************/

    /**
     * 用于为哈希表中的字段赋值 。
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果字段已经存在于哈希表中，旧值将被覆盖。
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hset(key, field, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 通过key同时设置 hash的多个field
     *
     * @param key
     * @param hash
     * @return
     */
    public Long hset(String key, Map<String, String> hash) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hset(key, hash);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略
     *
     * @param key
     * @param fields
     * @return 被成功删除字段的数量，不包括被忽略的字段
     */
    public Long hdel(String key, String... fields) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hdel(key, fields);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 用于查看哈希表的指定字段是否存在
     *
     * @param key
     * @param field
     * @return 如果哈希表含有给定字段，返回 1 。 如果哈希表不含有给定字段，或 key 不存在，返回 0
     */
    public Boolean hExists(String key, String field) {
        Jedis jedis = null;
        Boolean res = false;
        try {
            jedis = pool.getResource();
            res = jedis.hexists(key, field);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 用于返回哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hget(key, field);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 通过key同时设置 hash的多个field
     *
     * @param key
     * @param hash
     * @return
     */
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hmset(key, hash);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 用于返回哈希表中，一个或多个给定字段的值。
     * 如果指定的字段不存在于哈希表，那么返回一个 nil 值
     *
     * @param key
     * @param fields
     * @return
     */
    public List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        List<String> list = null;
        try {
            jedis = pool.getResource();
            list = jedis.hmget(key, fields);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return list;
    }

    /**
     * 用于返回哈希表中，所有的字段和值。
     * 在返回值里，紧跟每个字段名(field name)之后是字段的值(value)，所以返回值的长度是哈希表大小的两倍
     *
     * @param key
     * @return
     */
    public Map<String, String> hGetAll(String key) {
        Jedis jedis = null;
        Map<String, String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hgetAll(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;

    }

    /**
     * 用于为哈希表中的字段值加上指定增量值。
     * 增量也可以为负数，相当于对指定字段进行减法操作。
     * 如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
     * 对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。
     * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public long hincrBy(String key, String field, long value) {
        Jedis jedis = null;
        long res = 0L;
        try {
            jedis = pool.getResource();
            res = jedis.hincrBy(key, field, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 于为哈希表中的字段值加上指定浮点数增量值。
     * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Double hincrbyfloat(String key, String field, Double value) {
        Jedis jedis = null;
        Double res = 0d;
        try {
            jedis = pool.getResource();
            res = jedis.hincrByFloat(key, field, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 命令用于获取哈希表中的所有域（field）
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(String key) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hkeys(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 用于获取哈希表中字段的数量
     *
     * @param key
     * @return 当 key 不存在时，返回 0
     */
    public long hlen(String key) {
        Jedis jedis = null;
        long res = 0L;
        try {
            jedis = pool.getResource();
            res = jedis.hlen(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 用于为哈希表中不存在的的字段赋值 。
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果字段已经存在于哈希表中，操作无效。
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hsetnx(String key, String field, String value) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hsetnx(key, field, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 返回哈希表所有域(field)的值
     *
     * @param key
     * @return
     */
    public List<String> hvals(String key) {
        Jedis jedis = null;
        List<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.hvals(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /************************************ Redis 列表(List)*******************************************/

    /**
     * 将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误
     *
     * @param key
     * @param strs
     * @return 列表的长度
     */
    public Long lpush(String key, String... strs) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.lpush(key, strs);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 将一个值插入到已存在的列表头部，列表不存在时操作无效
     *
     * @param key
     * @param strs
     * @return 列表的长度
     */
    public Long lpushx(String key, String... strs) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.lpushx(key, strs);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 将一个或多个值插入到列表尾部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误
     *
     * @param key
     * @param strs
     * @return 列表的长度
     */
    public Long rpush(String key, String... strs) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.rpush(key, strs);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 将一个值插入到已存在的列表尾部，列表不存在时操作无效
     *
     * @param key
     * @param strs
     * @return 列表的长度
     */
    public Long rpushx(String key, String... strs) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.rpushx(key, strs);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        Jedis jedis = null;
        Long length = 0L;
        try {
            jedis = pool.getResource();
            length = jedis.llen(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return length;
    }


    /**
     * 通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     *
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key, long index) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.lindex(key, index);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 通过key从list的头部删除一个value,并返回该value
     *
     * @param key
     * @return
     */
    public String lpop(String key) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.lpop(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    public List<String> blpop(int timeout, String key) {
        Jedis jedis = null;
        List<String> list = null;
        try {
            jedis = pool.getResource();
            list = jedis.blpop(timeout, key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return list;
    }

    /**
     * 通过key从list的尾部删除一个value,并返回该value
     *
     * @param key
     * @return
     */
    public String rpop(String key) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.rpop(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        List<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.lrange(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * Lrem 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。
     * COUNT 的值可以是以下几种：
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0 : 移除表中所有与 VALUE 相等的值。
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    public Long lrem(String key, long count, String value) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.lrem(key, count, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 通过索引来设置元素的值。
     * 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public String lset(String key, Long index, String value) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.lset(key, index, value);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /*****************************Redis 集合(Set)**************************************/


    public Long sadd(String key, String... members) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.sadd(key, members);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 返回集合中元素的数量
     * 通过key获取set中value的个
     *
     * @param key
     * @return
     */
    public Long scard(String key) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.scard(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 返回给定集合之间的差集。不存在的集合 key 将视为空集。
     * 差集的结果来自前面的 FIRST_KEY ,而不是后面的 OTHER_KEY1，也不是整个 FIRST_KEY OTHER_KEY1..OTHER_KEYN 的差集
     *
     * @param keys
     * @return
     */
    public Set<String> sdiff(String... keys) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.sdiff(keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 将给定集合之间的差集存储在指定的集合中。如果指定的集合 key 已存在，则会被覆盖
     *
     * @param dstkey
     * @param keys
     * @return
     */
    public Long sdiffstore(String dstkey, String... keys) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.sdiffstore(dstkey, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * <p>
     * 通过key判断value是否是set中的元素
     * </p>
     *
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(String key, String member) {
        Jedis jedis = null;
        Boolean res = null;
        try {
            jedis = pool.getResource();
            res = jedis.sismember(key, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * <p>
     * 通过key获取set中所有的value
     * </p>
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.smembers(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 指定成员 member 元素从 source 集合移动到 destination 集合。
     * SMOVE 是原子性操作。
     * 如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。
     * 当 destination 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。
     * 当 source 或 destination 不是集合类型时，返回一个错误。
     *
     * @param srckey 要移除的
     * @param dstkey 添加
     * @param member set中的value
     * @return
     */
    public Long smove(String srckey, String dstkey, String member) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.smove(srckey, dstkey, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * <p>
     * 通过key随机删除个set中的value并返回该
     * </p>
     *
     * @param key
     * @return
     */
    public String spop(String key) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.spop(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * <p>
     * 通过key获取set中随机的value,不删除元
     * </p>
     *
     * @param key
     * @return
     */
    public String srandmember(String key) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.srandmember(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 被成功移除的元素的数量，不包括被忽略的元素。
     *
     * @param key
     * @param values
     * @return
     */
    public Long srem(String key, String... values) {
        Jedis jedis = null;
        Long removeTotal = null;
        try {
            jedis = pool.getResource();
            removeTotal = jedis.srem(key, values);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return removeTotal;
    }


    /**
     * 通过key返回有set的并
     *
     * @param keys 可以使一个string 也可以是个string数组
     * @return 返回给定集合的并集。不存在的集合 key 被视为空集
     */
    public Set<String> sunion(String... keys) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.sunion(keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 将给定集合的并集存储在指定的集合 destination 中。如果 destination 已经存在，则将其覆盖
     *
     * @param dstkey
     * @param keys   可以使一个string 也可以是个string数组
     * @return
     */
    public Long sunionstore(String dstkey, String... keys) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.sunionstore(dstkey, keys);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    public ScanResult<String> sscan(String key, String cursor) {
        Jedis jedis = null;
        ScanResult<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.sscan(key, cursor);

        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /*******************************************Redis 有序集合(sorted set)****************************/

    /**
     * 用于将一个或多个成员元素及其分数值加入到有序集当中。
     * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。
     * 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     * 注意： 在 Redis 2.4 版本以前， ZADD 每次只能添加一个元素。
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(String key, Double score, String member) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zadd(key, score, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 通过key向zset中添加value,score,其中score就是用来排序 如果该value已经存在则根据score更新元素
     *
     * @param key
     * @param scoreMembers
     * @return
     */
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 通过key返回zset中的value个数
     *
     * @param key
     * @return
     */
    public Long zcard(String key) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zcard(key);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 计算有序集合中指定分数区间的成员数量
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key, String min, String max) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zcount(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * Zincrby 命令对有序集合中指定成员的分数加上增量 increment
     * 可以通过传递一个负数值 increment ，让分数减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
     * 当 key 不存在，或分数不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。
     * 当 key 不是有序集类型时，返回一个错误。
     * 分数值可以是整数值或双精度浮点数
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Double zincrby(String key, double score, String member) {
        Jedis jedis = null;
        Double res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zincrby(key, score, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * Zinterstore 命令计算给定的一个或多个有序集的交集，其中给定 key 的数量必须以 numkeys 参数指定，并将该交集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和。
     *
     * @param deskey
     * @param sets
     * @return
     */
    public Long zinterstore(String deskey, String... sets) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zinterstore(deskey, sets);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 在计算有序集合中指定字典区间内成员数量
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zlexcount(String key, String min, String max) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zlexcount(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;

    }


    /**
     * 返回有序集中，指定区间内的成员。
     * 其中成员的位置按分数值递增(从小到大)来排序。
     * 具有相同分数值的成员按字典序(lexicographical order )来排列。
     * 如果你需要成员按
     * 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zRange(String key, Long start, Long end) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrange(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 字典区间返回有序集合的成员
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zRangeByLex(String key, String min, String max) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrangeByLex(key, min, max);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangeByScore(String key, String max, String min) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrangeByScore(key, max, min);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangeByScore(String key, double max, double min) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrangeByScore(key, max, min);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 通过key返回zset中value的排 下标从小到大排序
     * 返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrank(String key, String member) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrank(key, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 通过key删除在zset中指定的value
     *
     * @param key
     * @param members 可以使一个string 也可以是个string数组
     * @return
     */
    public Long zrem(String key, String... members) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrem(key, members);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 通过key删除给定区间内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

    /**
     * 通过key删除指定score内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zremrangeByScore(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 通过key将获取score从start到end中zset的value socre从大到小排序 当start0 end-1时返回全
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。
     * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。
     * 除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 通过key返回zset中value的排 下标从大到小排序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key, String member) {
        Jedis jedis = null;
        Long res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zrevrank(key, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }


    /**
     * 返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 null
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        Double res = null;
        try {
            jedis = pool.getResource();
            res = jedis.zscore(key, member);
        } catch (Exception e) {
            close(jedis);
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return res;
    }

}
