package com.chendeji.rongchen.ui.city.task;

import android.content.Context;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.dao.tables.city.CityTable;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.city.City;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public class CitySearchOnDateBaseTask extends BaseUITask<Void, Void, ReturnMes<List<String>>> {
    private final String mKeyWord;

    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     * @param keyWord
     */
    public CitySearchOnDateBaseTask(Context context, UITaskCallBack<ReturnMes<List<String>>> taskCallBack, String keyWord) {
        super(context, taskCallBack, true);
        this.mKeyWord = keyWord;
    }

//    @Override
//    protected void onPostExecute(ReturnMes<List<String>> listReturnMes) {
//        super.onPostExecute(listReturnMes);
//        if (AppConst.OK.equals(listReturnMes.status)) {
//            mTaskCallBack.onPostExecute(listReturnMes);
//        } else {
//            ErrorInfo errorInfo = listReturnMes.errorInfo;
////            ToastUtil.showLongToast(mContext, errorInfo.toString());
//        }
//    }

    @Override
    protected void fromDBDataSuccess(ReturnMes<List<String>> listReturnMes) {
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
            ErrorInfo errorInfo = listReturnMes.errorInfo;
//            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected void fromNetWorkDataSuccess(ReturnMes<List<String>> listReturnMes) {

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
        // 启动后台数据库查找
        List<String> cities = new ArrayList<>();
        List<City> cityList = City.find(City.class,
                CityTable.CITY_FULL_SPELL + " LIKE '%" + mKeyWord + "%' OR "
                        + CityTable.CITY_FIRST_SPELL + " LIKE '%" + mKeyWord + "%'",
                null,
                null,
                CityTable.CITY_FIRST_SPELL + " ASC",
                null);
        for (City city : cityList) {
            //满足任意上面的两个条件就添加到返回的列表中
            if (city.firstSpell.startsWith(mKeyWord) || city.fullSpell.startsWith(mKeyWord)) {
                cities.add(city.city);
            }
        }
        ReturnMes<List<String>> returnMes = null;
        if (cities.size() > 0) {
            returnMes = new ReturnMes<>();
            returnMes.object = cities;
            returnMes.status = AppConst.OK;
        } else {
            errorMsg = mContext.getResources().getString(R.string.database_nothing);
        }
        return returnMes;
    }

//    @Override
//    protected ReturnMes<List<String>> doInBackground(Void... params) {
//        // 启动后台数据库查找
//        List<String> cities = new ArrayList<>();
//        List<City> cityList = City.find(City.class,
//                CityTable.CITY_FULL_SPELL + " LIKE '%" + mKeyWord + "%' OR "
//                        + CityTable.CITY_FIRST_SPELL + " LIKE '%" + mKeyWord + "%'",
//                null,
//                null,
//                CityTable.CITY_FIRST_SPELL + " ASC",
//                null);
//        for (City city : cityList) {
//            //满足任意上面的两个条件就添加到返回的列表中
//            if (city.firstSpell.startsWith(mKeyWord) || city.fullSpell.startsWith(mKeyWord)) {
//                cities.add(city.city);
//            }
//        }
//        ReturnMes<List<String>> returnMes;
//        if (cities.size() > 0) {
//            returnMes = new ReturnMes<>();
//            returnMes.object = cities;
//            returnMes.status = AppConst.OK;
//        } else {
//            returnMes = new ReturnMes<>();
//            returnMes.status = AppConst.ERROR;
//            ErrorInfo info = new ErrorInfo(-1, mContext.getResources().getString(R.string.database_nothing));
//            returnMes.errorInfo = info;
//        }
//        return returnMes;
//    }


}
