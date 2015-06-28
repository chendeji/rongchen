package com.chendeji.rongchen.dao.tables.category;

import com.chendeji.rongchen.model.category.Category;
import com.chendeji.rongchen.model.category.SubCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 24/6/15.
 */
public class CategoryTable {

    public static final String CATEGORY_NAME = "CATEGORYNAME";
    public static final String SUBCATEGORIES = "SUBCATEGORIES";


    public static List<Category> getCategoryByCity(String city){
        List<Category> categories = new ArrayList<>();
        List<Category> tempCategories = Category.find(Category.class, null, new String[]{});
        for (Category category : tempCategories){

            List<SubCategory> subCategories = category.getSubcategories();
            for (SubCategory subCategory : subCategories){


            }

        }

        return null;
    }

}
