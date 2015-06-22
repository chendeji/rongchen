package com.example.chendeji.rongcheng;

import android.test.AndroidTestCase;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.server.AppServerConfig;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.ICityOperation;

import junit.framework.Assert;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public class CityOperationTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        AppServerConfig.getInstance().initServerFactory();
        super.setUp();
    }


    public void testGetCity() throws IOException, HttpException {
        ICityOperation cityOperation = AppServerFactory.getFactory().getCityOperation();
        ReturnMes<List<String>> returnMes = cityOperation.searchCity();

        Assert.assertNotNull(returnMes);
        List<String> cities = returnMes.object;
        Assert.assertNotNull(cities);
        Assert.assertTrue(cities.size() > 0);
    }
}
