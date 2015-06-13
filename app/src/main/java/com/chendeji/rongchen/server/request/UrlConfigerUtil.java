package com.chendeji.rongchen.server.request;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import com.chendeji.rongchen.server.AppConfigFactory;
import com.chendeji.rongchen.server.AppConst;

/**
 * @author chendeji
 * @Description:
 * @date 27/9/14 下午9:46
 * <p/>
 * ${tags}
 */
public class UrlConfigerUtil {

    /**
     * 添加访问参数
     * @param url 基本路径
     * @param path
     */
    public static void addPath(StringBuilder url, String path){

        if(TextUtils.isEmpty(url) || TextUtils.isEmpty(path)){
            return;
        }
        url.append("/");
        url.append(path);
    }

    /**
     * 构建点评网接口基本访问路径
     * @return 点评网的基本访问路径
     */
    public static StringBuilder getDPBaseUrl(){
        String url = AppConfigFactory.getInstance().getAppConfig(AppConst.AppBaseConst.DP_BASEURL);
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        return  builder;
    }

    public static boolean isStatusOK(JSONObject object){
        String status = object.getString(AppConst.ReturnMesConst.STATUS);
        if(AppConst.OK.equals(status)){
            return true;
        }
        return false;
    }

}
