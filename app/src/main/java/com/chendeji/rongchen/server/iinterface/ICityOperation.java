package com.chendeji.rongchen.server.iinterface;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.server.HttpException;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 21/6/15.
 */
public interface ICityOperation {

    ReturnMes<List<String>> searchCity() throws IOException, HttpException;

}
