package com.chendeji.rongchen.server;

import com.chendeji.rongchen.server.imp.CityOperation;
import com.chendeji.rongchen.server.imp.CommentOperation;
import com.chendeji.rongchen.server.imp.DealOperation;
import com.chendeji.rongchen.server.imp.MerchantOperation;

/**
 * 注入各个模块的接口工厂，单例
 * Created by chendeji on 27/9/14.
 */
public class AppServerConfig {

    private AppServerConfig(){};
    private static AppServerConfig cfg;
    public synchronized static AppServerConfig getInstance(){
        if (cfg == null){
            cfg = new AppServerConfig();
        }
        return cfg;
    }

    private static AppServerFactory appFactory;

    private String app_key = "7848668438";
    private String app_secret = "89dd01cb8b4245ea9e16a88ba24d96d4";
    private String base_url = "http://api.dianping.com";

    /**
     * 初始化应用的接口工厂
     */
    public void initServerFactory(){

        //应用常量配置导入
        AppConfigFactory factory = AppConfigFactory.getInstance();
        factory.setAppConfig(AppConst.AppBaseConst.APP_KEY, app_key);
        factory.setAppConfig(AppConst.AppBaseConst.APP_SECRET, app_secret);
        factory.setAppConfig(AppConst.AppBaseConst.DP_BASEURL, base_url);

        //注入接口工厂
        appFactory = AppServerFactory.getFactory();
        appFactory.setMerchantOperation(new MerchantOperation());
        appFactory.setCommentOperation(new CommentOperation());
        appFactory.setDealOperation(new DealOperation());
        appFactory.setCityOperation(new CityOperation());


    }

    /**
     * 将注入的工厂类全部回收
     */
    public static void destoryFactory(){
        cfg = null;
        if (appFactory != null){
            appFactory.setMerchantOperation(null);
            appFactory.setCommentOperation(null);
            appFactory.setDealOperation(null);
            appFactory.setCityOperation(null);
        }

    }

}
