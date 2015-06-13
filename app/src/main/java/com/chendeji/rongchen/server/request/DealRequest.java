package com.chendeji.rongchen.server.request;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.httpCommon.DPHttpRequestClient;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.parser.DealParser;
import com.chendeji.rongchen.server.parser.ErrorInfoParser;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by chendeji on 27/5/15.
 */
public class DealRequest {

    /**
     * @函数名称  :getDeal
     * @brief
     * @see
     * @param deal_id
     * @return return mes
     * @author  : chendeji
     * @date  : Wed May 27 17:11:09 CST 2015
     */
    public ReturnMes<Deal> getDeal(String deal_id) throws IOException, HttpException {

        if (TextUtils.isEmpty(deal_id))
            return null;
        StringBuilder url = UrlConfigerUtil.getDPBaseUrl();
        UrlConfigerUtil.addPath(url, AppConst.DEAL_BASE_URL);
        UrlConfigerUtil.addPath(url, AppConst.GET_SINGLE_DEAL);

        //拼接参数
        HashMap<String,String> parame = new HashMap<String, String>();
        parame.put("deal_id", deal_id);

        //发送请求并获取服务器返回结果
        StringBuilder receive = new StringBuilder();
        int retCode = DPHttpRequestClient.doGet(url.toString(), parame, receive);
        JSONObject object = JSON.parseObject(receive.toString());
        if(retCode == HttpStatus.SC_OK){
            if(UrlConfigerUtil.isStatusOK(object)){
                //TODO 解析服务端返回的团购的详细信息
                Deal deal = new DealParser().parse(object);
                ReturnMes returnMes = new ReturnMes(retCode,"");
                returnMes.status = object.getString(AppConst.ReturnMesConst.STATUS);
                returnMes.object = deal;
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
