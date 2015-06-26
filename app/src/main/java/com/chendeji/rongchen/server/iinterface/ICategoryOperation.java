package com.chendeji.rongchen.server.iinterface;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.category.Category;
import com.chendeji.rongchen.server.HttpException;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 24/6/15.
 */
public interface ICategoryOperation {

    ReturnMes<List<Category>> getCategory(String city) throws IOException, HttpException;

}
