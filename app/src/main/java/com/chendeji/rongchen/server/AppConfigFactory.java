package com.chendeji.rongchen.server;

import java.util.HashMap;

/**
 * 用于存放应用的常量
 * Created by chendeji on 27/9/14.
 */
public class AppConfigFactory {

    private AppConfigFactory(){}
    private static AppConfigFactory factory;

    public synchronized static AppConfigFactory getInstance(){
        if (factory == null){
            factory = new AppConfigFactory();
        }
        return factory;
    }

    private HashMap<String,String> appConfig = new HashMap<String, String>();

    public void setAppConfig(String key,String value){
        appConfig.put(key, value);
    }

    public String getAppConfig(String key){
        return appConfig.get(key);
    }

}
