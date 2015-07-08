package com.chendeji.rongchen.ui.city.task;

import android.content.Context;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.city.RecentSearchCity;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 26/6/15.
 */
public class GetRecentSearchCityTask extends BaseUITask<Void, Void, ReturnMes<List<String>>> {
    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public GetRecentSearchCityTask(Context context, UITaskCallBack<ReturnMes<List<String>>> taskCallBack) {
        super(context, taskCallBack, true);
    }

//    @Override
//    protected void onPostExecute(ReturnMes<List<String>> listReturnMes) {
//        super.onPostExecute(listReturnMes);
//
//    }

    @Override
    protected void fromDBDataSuccess(ReturnMes<List<String>> listReturnMes) {
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
//            ErrorInfo errorInfo = listReturnMes.errorInfo;
//            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected void fromNetWorkDataSuccess(ReturnMes<List<String>> listReturnMes) {
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
//            ErrorInfo errorInfo = listReturnMes.errorInfo;
//            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected void fromDBDataError(String errorMsg) {

    }

    @Override
    protected void fromNetWorkDataError(String errorMsg) {

    }

    @Override
    protected ReturnMes<List<String>> getDataFromNetwork() {
        return null;
    }

    @Override
    protected ReturnMes<List<String>> getDataFromDB() {
        List<RecentSearchCity> recentSearchCities = SettingFactory.getInstance().getRecentSearchCity();
        List<String> stringList = new ArrayList<>();
        ReturnMes<List<String>> returnMes = null;
        if (recentSearchCities != null && !recentSearchCities.isEmpty()){
            for (RecentSearchCity city : recentSearchCities){
                stringList.add(city.city_name);
            }
            returnMes = new ReturnMes<>();
            returnMes.status = AppConst.OK;
            returnMes.object = stringList;
        } else {
            errorMsg = mContext.getString(R.string.data_no_prepare_please_wait);
        }
        return returnMes;
    }

//    @Override
//    protected ReturnMes<List<String>> doInBackground(Void... params) {
//
//        List<RecentSearchCity> recentSearchCities = SettingFactory.getInstance().getRecentSearchCity();
//        List<String> stringList = new ArrayList<>();
//        ReturnMes<List<String>> returnMes = new ReturnMes<>();
//        if (recentSearchCities != null && !recentSearchCities.isEmpty()){
//            for (RecentSearchCity city : recentSearchCities){
//                stringList.add(city.city_name);
//            }
//
//            returnMes.status = AppConst.OK;
//            returnMes.object = stringList;
//        } else {
//            returnMes.status = AppConst.OK;
//        }
//
//        return returnMes;
//    }
}
