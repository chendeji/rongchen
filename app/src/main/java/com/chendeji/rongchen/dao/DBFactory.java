package com.chendeji.rongchen.dao;

import android.content.Context;

import com.orm.SugarContext;

/**
 * Created by chendeji on 22/6/15.
 */
public class DBFactory {
    private static DBFactory ourInstance = new DBFactory();

    public static DBFactory getInstance() {
        return ourInstance;
    }

    private DBFactory() {
    }

    public void init(Context context){
        initDB(context);

    }

    private void initDB(Context context){
        SugarContext.init(context);

        //创建表格


    }

    public void destroy(){
        SugarContext.terminate();
    }

}
