package com.chendeji.rongchen.server.imp;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.category.Category;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.ICategoryOperation;
import com.chendeji.rongchen.server.request.CategoryRequest;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 24/6/15.
 */
public class CategoryOperation implements ICategoryOperation {
    @Override
    public ReturnMes<List<Category>> getCategory(String city) throws IOException, HttpException {
        CategoryRequest request = new CategoryRequest();
        return request.getCategory(city);
    }
}
