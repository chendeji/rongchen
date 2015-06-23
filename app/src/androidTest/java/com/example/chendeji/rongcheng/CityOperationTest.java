package com.example.chendeji.rongcheng;

import android.test.AndroidTestCase;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.dao.DBFactory;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.city.City;
import com.chendeji.rongchen.server.AppServerConfig;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.ICityOperation;
import com.chendeji.rongchen.ui.city.task.CitySearchOnDateBaseTask;
import com.chendeji.rongchen.ui.city.task.CitySearchTask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.orm.SugarTransactionHelper;

import junit.framework.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public class CityOperationTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        AppServerConfig.getInstance().initServerFactory();
        DBFactory.getInstance().init(getContext());
        super.setUp();
    }


    public void testGetCity() throws IOException, HttpException {
        ICityOperation cityOperation = AppServerFactory.getFactory().getCityOperation();
        ReturnMes<List<String>> returnMes = cityOperation.searchCity();

        Assert.assertNotNull(returnMes);
        List<String> cities = returnMes.object;
        Assert.assertNotNull(cities);
        Assert.assertTrue(cities.size() > 0);

        for (String city : cities){
            if ("长乐".equals(city)){
                Logger.i("chendeji","找到长乐");
                break;
            }
        }
    }

    public void testSaveCityInDB() throws IOException, HttpException {
        ICityOperation cityOperation = AppServerFactory.getFactory().getCityOperation();
        ReturnMes<List<String>> returnMes = cityOperation.searchCity();

        Assert.assertNotNull(returnMes);
        List<String> cities = returnMes.object;
        Assert.assertNotNull(cities);
        Assert.assertTrue(cities.size() > 0);

        final List<String> list_cities = returnMes.object;
        SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
            @Override
            public void manipulateInTransaction() {
                City city;
                for (String str_city : list_cities) {
                    city = new City();
                    city.city = str_city;
                    city.save();
                }
            }
        });
        testGetCityFromDB();
    }

    public void testGetCityFromDB() {
        List<String> cities = new ArrayList<>();
        Iterator<City> cityIterator = City.findAll(City.class);
        City city;
        ReturnMes<List<String>> returnMes;
        while (cityIterator.hasNext()) {
            city = cityIterator.next();
            cities.add(city.city);
        }
        Assert.assertNotNull(cities);
        Assert.assertTrue(cities.size() > 0);

    }
}
