package com.chendeji.rongchen.server;

/**
 * 用于定义应用的常量
 * Created by chendeji on 27/9/14.
 */
public final class AppConst {

    public static final String METADATA = "v1/metadata";
    public static final String GET_CITIES_WITH_BUSINESSES = "get_cities_with_businesses?";
    public static final String GET_CATEGORIES_WITH_BUSINESSES = "get_categories_with_businesses?";

    public static final String MERCHANT_BASE_URL = "v1/business";
    public static final String FIND_MERCHANTS = "find_businesses?";
    public static final String GET_SINGLE_BUSINESS = "get_single_business?";

    public static final String COMMENT_BASE_URL = "v1/review";
    public static final String GET_RECENT_REVIEWS = "get_recent_reviews?";

    public static final String DEAL_BASE_URL = "v1/deal";
    public static final String GET_SINGLE_DEAL = "get_single_deal?";

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

    public static interface AppBaseConst{
        /** 大众点评基本访问路径*/
        String DP_BASEURL = "base_url";
        /** 应用id*/
        String APP_KEY = "appkey";
        /** 应用secret*/
        String APP_SECRET = "secret";
        /** 应用登入的基本地址*/
        String APP_LOGIN_URL = "http://m.dianping.com/my";
        /** 大众点评的H5首页*/
        String DIANPING_H5_FIRST_PAGE = "http://m.dianping.com/";
        /** 大众点评退出账号H5地址*/
        String DIANPING_LOGOUT = "http://m.dianping.com/logout";
        /** 大众点评H5页面首页*/
        String DIANPING_INDEX = "http://m.dianping.com/tuan";
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
        String DEFUALT_CATEGORY = "美食";
        String ALL = "全部";
    }



}
