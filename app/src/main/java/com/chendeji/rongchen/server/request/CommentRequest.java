package com.chendeji.rongchen.server.request;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.model.Platform;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.comment.Comment;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.httpCommon.DPHttpRequestClient;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.parser.CommentListParser;
import com.chendeji.rongchen.server.parser.ErrorInfoParser;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chendeji on 25/5/15.
 */
public final class CommentRequest {

    public ReturnMes<List<Comment>> findTenRecordComment(long business_id) throws IOException, HttpException {

        StringBuilder url = UrlConfigerUtil.getDPBaseUrl();
        UrlConfigerUtil.addPath(url, AppConst.COMMENT_BASE_URL);
        UrlConfigerUtil.addPath(url, AppConst.GET_RECENT_REVIEWS);

        //拼接参数
        HashMap<String,String> parame = new HashMap<String, String>();
        if (business_id <= 0){
            return null;
        }
        int limit = 10;
        parame.put("business_id", String.valueOf(business_id));
        parame.put("platform", String.valueOf(Platform.HTML5.getValue()));

        //发送请求并获取服务器返回结果
        StringBuilder receive = new StringBuilder();
        int retCode = DPHttpRequestClient.doGet(url.toString(), parame, receive);
        JSONObject object = JSON.parseObject(receive.toString());
        if(retCode == HttpStatus.SC_OK){
            if(UrlConfigerUtil.isStatusOK(object)){
                List<Comment> commentList = new CommentListParser().parse(object);
                JSONObject all_comments_url = object.getJSONObject("additional_info");
                String more_comments = all_comments_url.getString("more_reviews_url");
                ReturnMes returnMes = new ReturnMes(retCode,"");
                returnMes.status = object.getString(AppConst.ReturnMesConst.STATUS);
                returnMes.object = commentList;
                if (!TextUtils.isEmpty(more_comments)){
                    returnMes.moreInfo = all_comments_url;
                }
                return returnMes;
            }else{
                ReturnMes returnMes = new ErrorInfoParser().parse(object);
                return returnMes;
            }
        }else{
            //服务端请求失败
            throw new HttpException(retCode, receive.toString());
        }
    }

}
