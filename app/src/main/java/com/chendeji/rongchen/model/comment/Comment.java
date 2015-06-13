package com.chendeji.rongchen.model.comment;

/**
 * 点评实体类
 * Created by chendeji on 25/5/15.
 */
public class Comment {

    //评论ID
    public long review_id;

    //用户昵称
    public String user_nickname;

    //创建评论时间
    public String created_time;

    //点评内容
    public String text_excerpt;


    //三个评分指标
    public int product_rating;
    public int decoration_rating;
    public int service_rating;

    //该条点评具体详细信息
    public String review_url;


}
