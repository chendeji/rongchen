package com.chendeji.rongchen.ui.city.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.ChineseSpelling;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.city.City;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.orm.SugarTransactionHelper;

import java.io.IOException;
import java.util.ArrayList;
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
            //先查找数据库，如果数据库有东西就直接用数据库的。
            List<City> cityList = City.find(City.class, null, new String[]{});
            ReturnMes<List<String>> cities = null;
            if (cityList.size() == 0){
                cities = AppServerFactory.getFactory().getCityOperation().searchCity();
                // 将数据填充到数据库中
                final List<String> list_cities = cities.object;
                long beginTime = System.currentTimeMillis();
                final ChineseSpelling spelling = ChineseSpelling.getInstance();
                SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                    @Override
                    public void manipulateInTransaction() {
                        City city;
                        for (String str_city : list_cities) {
                            city = new City();
                            city.city = str_city;
//                            city.firstSpell = PinYin.getFirstSpell(str_city);
//                            city.fullSpell = PinYin.getPinYin(str_city);
                            city.firstSpell = spelling.getFirstSpelling(str_city);
                            city.fullSpell = spelling.getSelling(str_city);
                            city.save();
                        }
                    }
                });
                long endTime = System.currentTimeMillis();
                Logger.i("chendeji","插入数据库消耗的时间："+(endTime - beginTime) / 1000);   //查看耗时多少秒
            }else {
                List<String> stringList = new ArrayList<>();
                for (City city : cityList){
                    stringList.add(city.city);
                }
                cities = new ReturnMes<>();
                cities.status = AppConst.OK;
                cities.object = stringList;
            }
            return cities;
        } catch (IOException e) {
            Logger.i(this.getClass().getSimpleName(), "解析错误");
        } catch (HttpException e) {
            Logger.i(this.getClass().getSimpleName(), "网络错误");
        }
        return null;
    }
}
