package com.chendeji.rongchen.model.comment;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

/**
 * 点评实体类
 * Created by chendeji on 25/5/15.
 */
@Table(name = "Comment")
public class Comment extends SugarRecord{

    //评论ID
    @Column(name = "review_id", unique = true, notNull = true)
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

    @Override
    public String toString() {
        return "Comment{" +
                "review_id=" + review_id +
                ", user_nickname='" + user_nickname + '\'' +
                ", created_time='" + created_time + '\'' +
                ", text_excerpt='" + text_excerpt + '\'' +
                ", product_rating=" + product_rating +
                ", decoration_rating=" + decoration_rating +
                ", service_rating=" + service_rating +
                ", review_url='" + review_url + '\'' +
                '}';
    }
}
