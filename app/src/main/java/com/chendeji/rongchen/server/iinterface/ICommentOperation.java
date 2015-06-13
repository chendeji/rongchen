package com.chendeji.rongchen.server.iinterface;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.comment.Comment;
import com.chendeji.rongchen.server.HttpException;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 24/5/15.
 */
public interface ICommentOperation {

    /**
     * @param business_id
     * @return return mes {@link com.chendeji.rongchen.model.ReturnMes} 这个函数中这个对象中的{@link com.chendeji.rongchen.model.ReturnMes#moreInfo}还有信息，是全部的点评信息的字符串链接
     * @throws IOException
     * @throws HttpException
     * @函数名称 :findTenRecordComment
     * @brief
     * @author : chendeji
     * @date : Mon May 25 13:17:24 CST 2015
     * @see
     */
    ReturnMes<List<Comment>> findTenRecordComment(long business_id) throws IOException, HttpException;

}
