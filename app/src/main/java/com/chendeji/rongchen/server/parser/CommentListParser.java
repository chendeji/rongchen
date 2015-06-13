package com.chendeji.rongchen.server.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.model.comment.Comment;

import java.util.List;

/**
 * Created by chendeji on 25/5/15.
 */
public class CommentListParser implements Parsable<List<Comment>>{
    private static final String COMMENT_KEY = "reviews";
    @Override
    public List<Comment> parse(JSONObject object) {
        if(object == null || object.isEmpty()){
            return null;
        }
        List<Comment> commentList = null;
        String array = object.getString(COMMENT_KEY);
        if (array != null){
            commentList = JSON.parseArray(array, Comment.class);
            return commentList;
        }
        return null;
    }
}
