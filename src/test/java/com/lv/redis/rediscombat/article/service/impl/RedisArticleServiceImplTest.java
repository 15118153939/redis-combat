package com.lv.redis.rediscombat.article.service.impl;

import com.lv.redis.rediscombat.article.service.RedisArticleService;
import com.lv.redis.rediscombat.util.JedisUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Set;


/**
 * @author 吕明亮
 * @Date : 2019/9/3 17:06
 * @Description:
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedisArticleServiceImplTest {


    private RedisArticleService redisArticleService = new RedisArticleServiceImpl();

    @Test
    public void postArticle() throws Exception {
        String userId = "001"; //用户ID 001
        String title = "The road to west";
        String content = "About body and mental health";
        String link = "www.miguo.com";
        //发布文章，返回文章ID
        String articleId = redisArticleService.postArticle(title, content, link, userId);
        System.out.println("刚发布了一篇文章，文章ID为: " + articleId);
        System.out.println("文章所有属性值内容如下:");
        Map<String, String> articleData = redisArticleService.hgetAll("article:" + articleId);
        for (Map.Entry<String, String> entry : articleData.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
    }

    @Test
    public void hgetAll() throws Exception {
    }

    @Test
    public void articleVote() throws Exception {

        String userId = "008";
        String articleId = "1";

        System.out.println(userId + "开始对文章" + "article:" + articleId + "进行投票啦~~~~~");
        //cang用户给James的文章投票
        redisArticleService.articleVote(userId, articleId);//article:1
        //投票完后，查询当前文章的投票数votes
        String votes = redisArticleService.hget("article:" + articleId, "votes");

        System.out.println("article:" + articleId + "这篇文章的投票数从redis查出来结果为: " + votes);

    }

    @Test
    public void hget() throws Exception {
    }

    @Test
    public void getArticles() throws Exception {
    }


    @Test
    public void reSet() {

        JedisUtil jedisUtil = new JedisUtil();
        Set<String> keys = jedisUtil.keys("*");

        if (keys != null) {
            keys.stream().forEach(key -> jedisUtil.del(key));
        }


    }
}