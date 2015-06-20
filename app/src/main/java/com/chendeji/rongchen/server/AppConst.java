package com.chendeji.rongchen.server;

/**
 * 用于定义应用的常量
 * Created by chendeji on 27/9/14.
 */
public class AppConst {

    public static String MERCHANT_BASE_URL = "v1/business";
    public static String FIND_MERCHANTS = "find_businesses?";
    public static String GET_SINGLE_BUSINESS = "get_single_business?";

    public static String COMMENT_BASE_URL = "v1/review";
    public static String GET_RECENT_REVIEWS = "get_recent_reviews?";

    public static String DEAL_BASE_URL = "v1/deal";
    public static String GET_SINGLE_DEAL = "get_single_deal?";

    public static String OK = "OK";
    public static String ERROR = "ERROR";

    public static interface AppBaseConst{
        /** 大众点评基本访问路径*/
        String DP_BASEURL = "base_url";
        /** 应用id*/
        String APP_KEY = "appkey";
        /** 应用secret*/
        String APP_SECRET = "secret";
    }

    public static interface ReturnMesConst{
        /** 服务器返回状态*/
        String STATUS = "status";
        /** 该类型数据总共数据*/
        String TOTAL_COUNT = "total_count";
        /** 返回的数量*/
        String COUNT = "count";
        /** 返回的错误信息*/
        String ERROR = "error";
    }

    public static interface RequestParams{
        String DEFUALT_CITY = "北京";
    }



}
