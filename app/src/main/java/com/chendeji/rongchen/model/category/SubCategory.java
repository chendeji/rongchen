package com.chendeji.rongchen.model.category;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chendeji.rongchen.dao.tables.category.SubCategoryTable;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 24/6/15.
 */
@Table(name = "SubCategory")
public class SubCategory extends SugarRecord{

    @Column(name = SubCategoryTable.CATEGORY_NAME, unique = true, notNull = true)
    public String category_name;
    public String parent_category_name;

    public String str_subcategories;

    public String str_cities;

    @Ignore
    public List<String> subcategories;

    public List<String> getSubcategories(){
        if (subcategories != null && !subcategories.isEmpty()){
            return subcategories;
        }
        subcategories = JSON.parseArray(str_subcategories, String.class);
        return subcategories;
    }

    public void addBelongCity(String city){
        String oldStr_cities = null;
        List<SubCategory> subCategories = find(SubCategory.class, SubCategoryTable.CATEGORY_NAME + "=?", new String[]{category_name});
        if (subCategories != null && !subCategories.isEmpty()) {
            SubCategory subCategory = subCategories.get(0);
            oldStr_cities = subCategory.str_cities;
            if (TextUtils.isEmpty(oldStr_cities)) {
                str_cities = city;
            } else {
                if (!oldStr_cities.contains(city)) {
                    str_cities = oldStr_cities + "," + city;
                }
            }
        } else {
            str_cities = city;
        }
    }

}
