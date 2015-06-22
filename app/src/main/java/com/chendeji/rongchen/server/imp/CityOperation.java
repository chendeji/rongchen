package com.chendeji.rongchen.server.imp;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.ICityOperation;
import com.chendeji.rongchen.server.request.CityDataRequest;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public class CityOperation implements ICityOperation {
    @Override
    public ReturnMes<List<String>> searchCity() throws IOException, HttpException {

        CityDataRequest request = new CityDataRequest();
        return request.searchCity();
    }
}
