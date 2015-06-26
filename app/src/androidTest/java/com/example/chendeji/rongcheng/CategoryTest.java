package com.example.chendeji.rongcheng;

import android.test.AndroidTestCase;

import com.chendeji.rongchen.dao.DBFactory;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.category.Category;
import com.chendeji.rongchen.model.category.SubCategory;
import com.chendeji.rongchen.server.AppServerConfig;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.server.iinterface.ICategoryOperation;

import junit.framework.Assert;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 25/6/15.
 */
public class CategoryTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        AppServerConfig.getInstance().initServerFactory();
        DBFactory.getInstance().init(getContext());
        super.setUp();
    }

    public void testGetCategory() throws IOException, HttpException {
        ICategoryOperation operation = AppServerFactory.getFactory().getCategoryOperation();
        ReturnMes<List<Category>> returnMes = operation.getCategory("福州");
        Assert.assertNotNull(returnMes);
        List<Category> categories = returnMes.object;
        Assert.assertNotNull(categories);
        Assert.assertTrue(categories.size() > 0);

        for (Category category : categories){
            List<SubCategory> subcategories = category.subcategories;
            for (SubCategory subCategory : subcategories){
                subCategory.save();
            }
            category.save();
        }
    }

    public void testGetSubCategory(){
        SubCategory subCategory = SubCategory.findById(SubCategory.class, 7);
        List<String> str = subCategory.getSubcategories();
        Assert.assertNotNull(str);
        Assert.assertTrue(str.size() > 0);
    }
}
