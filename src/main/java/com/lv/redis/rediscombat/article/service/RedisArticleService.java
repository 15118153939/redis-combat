package com.lv.redis.rediscombat.article.service;

import java.util.List;
import java.util.Map;

/**
 * @author 吕明亮
 * @Date : 2019/9/3 16:57
 * @Description:
 */
public interface RedisArticleService {

    /**
     * 文章提交发布
     *
     * @param title   标题
     * @param content 内容
     * @param link    链接
     * @param userId  用户ID
     * @return 文章的ID
     */
    String postArticle(String title, String content, String link, String userId);

    /**
     *
     * @param key
     * @return
     */
    Map<String, String> hgetAll(String key);

    /**
     * 文章投票
     * @param userId  用戶ID
     * @param articleId 文章ID（article:001）  //001
     */
    void articleVote(String userId, String articleId);

    String hget(String key, String votes);

    /**
     * 文章列表查询（分页）
     * @param page
     * @param key
     * @return redis查询结果
     */
    List<Map<String, String>> getArticles(int page, String key);
}
