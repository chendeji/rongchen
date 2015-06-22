package com.chendeji.rongchen.ui.city.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public class CitySearchTask extends BaseUITask<Void, Void, ReturnMes<List<String>>> {
    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public CitySearchTask(Context context, UITaskCallBack<ReturnMes<List<String>>> taskCallBack) {
        super(context, taskCallBack);
    }

    @Override
    protected void onPostExecute(ReturnMes<List<String>> listReturnMes) {
        super.onPostExecute(listReturnMes);
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
            ErrorInfo errorInfo = listReturnMes.errorInfo;
            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected ReturnMes<List<String>> doInBackground(Void... params) {
        try {
            ReturnMes<List<String>> cities = AppServerFactory.getFactory().getCityOperation().searchCity();
            //TODO 将数据填充到数据库中

            return cities;
        } catch (IOException e) {
            Logger.i(this.getClass().getSimpleName(), "解析错误");
        } catch (HttpException e) {
            Logger.i(this.getClass().getSimpleName(), "网络错误");
        }
        return null;
    }
}
