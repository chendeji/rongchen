package com.chendeji.rongchen.server.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.httpCommon.DPHttpRequestClient;
import com.chendeji.rongchen.server.parser.CityListParser;
import com.chendeji.rongchen.server.parser.ErrorInfoParser;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public class CityDataRequest {

    public ReturnMes<List<String>> searchCity() throws IOException, HttpException {
        StringBuilder url = UrlConfigerUtil.getDPBaseUrl();
        UrlConfigerUtil.addPath(url, AppConst.METADATA);
        UrlConfigerUtil.addPath(url, AppConst.GET_CITIES_WITH_BUSINESSES);

        //拼接参数
        HashMap<String, String> parame = new HashMap<String, String>();

        //发送请求并获取服务器返回结果
        StringBuilder receive = new StringBuilder();
        int retCode = DPHttpRequestClient.doGet(url.toString(), parame, receive);
        JSONObject object = JSON.parseObject(receive.toString());
        if (retCode == HttpStatus.SC_OK) {
            if (UrlConfigerUtil.isStatusOK(object)) {
                List<String> cityList = new CityListParser().parse(object);
                ReturnMes returnMes = new ReturnMes(retCode, "");
                returnMes.status = object.getString(AppConst.ReturnMesConst.STATUS);
                returnMes.object = cityList;
                return returnMes;
            } else {
                ReturnMes returnMes = new ErrorInfoParser().parse(object);
                return returnMes;
            }
        } else {
            //服务端请求失败
            throw new HttpException(retCode, receive.toString());
        }
    }
}
