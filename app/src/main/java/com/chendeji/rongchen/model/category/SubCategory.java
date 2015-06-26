package com.chendeji.rongchen.model.category;

import com.alibaba.fastjson.JSON;
import com.chendeji.rongchen.dao.tables.category.SubCategoryTable;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

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

    @Ignore
    public List<String> subcategories;

    public List<String> getSubcategories(){
        if (subcategories != null && !subcategories.isEmpty()){
            return subcategories;
        }
        subcategories = JSON.parseArray(str_subcategories, String.class);
        return subcategories;
    }

}
