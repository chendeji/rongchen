package com.chendeji.rongchen.ui.city.task;

import android.content.Context;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public class CitySearchOnDateBaseTask extends BaseUITask<Void, Void, ReturnMes<List<String>>> {
    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public CitySearchOnDateBaseTask(Context context, UITaskCallBack<ReturnMes<List<String>>> taskCallBack) {
        super(context, taskCallBack);
    }

    @Override
    protected ReturnMes<List<String>> doInBackground(Void... params) {
        //TODO 启动后台数据库查找

        return null;
    }


}
