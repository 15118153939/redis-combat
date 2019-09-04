package com.lv.redis.rediscombat.article.basic;

/**
 * @author 吕明亮
 * @Date : 2019/9/3 16:54
 * @Description:
 */
public class Constants {
    /**
     * 文章发布7天后失效，不能投票
     */
    public static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    /**
     * 获取一票后文章分值加400
     */
    public static final int VOTE_SCORE = 400;
    /**
     * 分页查询每页显示25条
     */
    public static final int ARTICLES_PER_PAGE = 25;
}
