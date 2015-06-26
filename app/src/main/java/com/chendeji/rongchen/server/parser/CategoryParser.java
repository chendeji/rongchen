package com.chendeji.rongchen.server.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chendeji.rongchen.model.category.Category;
import com.chendeji.rongchen.model.category.SubCategory;

import java.util.List;

/**
 * Created by chendeji on 24/6/15.
 */
public class CategoryParser implements Parsable<List<Category>> {
    private static final String CATEGORY_KEY = "categories";

    @Override
    public List<Category> parse(JSONObject object) {

        if(object == null || object.isEmpty()){
            return null;
        }
        String array = object.getString(CATEGORY_KEY);
        if (!TextUtils.isEmpty(array)){
            List<Category> categories = JSON.parseArray(array, Category.class);
            for (Category category : categories){
                List<SubCategory> subcategories = category.subcategories;
                if (subcategories == null || subcategories.isEmpty())
                    continue;
                for (SubCategory subCategory : subcategories){
                    subCategory.parent_category_name = category.category_name;
                    subCategory.str_subcategories = JSON.toJSONString(subCategory.subcategories);
                }
            }
            return categories;
        }

        return null;
    }
}
