package com.chendeji.rongchen.server.imp;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.comment.Comment;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.ICommentOperation;
import com.chendeji.rongchen.server.request.CommentRequest;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 25/5/15.
 */
public class CommentOperation implements ICommentOperation {
    @Override
    public ReturnMes<List<Comment>> findTenRecordComment(long business_id) throws IOException, HttpException {

        CommentRequest request = new CommentRequest();
        ReturnMes<List<Comment>> returnMes = request.findTenRecordComment(business_id);
        return returnMes;
    }
}
