package com.lv.redis.rediscombat.article.service.impl;

import com.lv.redis.rediscombat.article.basic.Constants;
import com.lv.redis.rediscombat.article.service.RedisArticleService;
import com.lv.redis.rediscombat.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 吕明亮
 * @Date : 2019/9/3 16:57
 * @Description:
 */
@Service("redisArticleServiceImpl")
public class RedisArticleServiceImpl implements RedisArticleService {

    @Autowired
    JedisUtil jedisUtil = new JedisUtil();

    @Override
    public String postArticle(String title, String content, String link, String userId) {

        //文章ID，自增 UUID 主键    1－－－id 主键
        Long id = jedisUtil.incr("article:");
        String articleId = String.valueOf(id);

        //用来记录投票( key 设计为voted：文章id)  key  voted:1  set key value
        String voted = "voted:" + articleId;

        //将投票的用户记录到voted:1键集合来……001
        jedisUtil.sadd(voted, userId);
        //设置失效时间
        jedisUtil.expire(voted, Constants.ONE_WEEK_IN_SECONDS);

        //删数据之前,是不是要转移一下

        //时间
        long now = System.currentTimeMillis() / 1000;
        //生成文章ID  二维数据   hash
        String articleKey = "article:" + articleId;
        //article:1
        HashMap<String, String> articleData = new HashMap<String, String>();
        articleData.put("title", title);
        articleData.put("link", link);
        articleData.put("user", userId);
        articleData.put("now", String.valueOf(now));
        articleData.put("votes", "1");

        jedisUtil.hmset(articleKey, articleData);

        //根据分值排序文章的有序集合
        jedisUtil.zadd("score:info", Double.valueOf(now + Constants.VOTE_SCORE), articleKey);
        //根据文章发布时间排序文章的有序集合
        jedisUtil.zadd("time:", Double.valueOf(now), articleKey);

        return articleId;

    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return jedisUtil.hGetAll(key);
    }

    @Override
    public void articleVote(String userId, String articleId) {


        //计算投票截止时间
        long cutoff = (System.currentTimeMillis() / 1000) - Constants.ONE_WEEK_IN_SECONDS;
        //检查是否还可以对文章进行投票,如果该文章的发布时间比截止时间小，则已过期，不能进行投票
        if (jedisUtil.zscore("time:", "article:" + articleId) < cutoff) {
            return;
        }


        //将投票的用户加入到键为voted:1的集合中，表示该用户已投过票了 voted:1  set集合里来
        //0 并不1

        if (jedisUtil.sadd("voted:" + articleId, userId) == 1) {
            jedisUtil.zincrby("score:info", Constants.VOTE_SCORE, "article:" + articleId);//分值加400
            jedisUtil.hincrBy("article:" + articleId, "votes", 1L);//投票数加1
        }

    }

    @Override
    public String hget(String key, String votes) {
        return jedisUtil.hget(key, votes);
    }

    @Override
    public List<Map<String, String>> getArticles(int page, String key) {
        return null;
    }
}
