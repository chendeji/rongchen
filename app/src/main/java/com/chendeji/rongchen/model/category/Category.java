package com.chendeji.rongchen.model.category;

import com.chendeji.rongchen.dao.tables.category.CategoryTable;
import com.chendeji.rongchen.dao.tables.category.SubCategoryTable;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by chendeji on 24/6/15.
 */
@Table(name = "Category")
public class Category extends SugarRecord {

    @Column(name = CategoryTable.CATEGORY_NAME, unique = true, notNull = true)
    public String category_name;

    @Ignore
    public List<SubCategory> subcategories;

    public List<SubCategory> getSubcategories() {
        if (subcategories != null && !subcategories.isEmpty()) {
            return subcategories;
        }
        subcategories = find(SubCategory.class, SubCategoryTable.PARENT_CATEGORY_NAME + "=?", new String[]{category_name});
        return subcategories;
    }

    public List<SubCategory> getSubCategoryByCity(String city) {
        subcategories = find(SubCategory.class, SubCategoryTable.PARENT_CATEGORY_NAME + "=? AND "
                + SubCategoryTable.STR_CITIES + " LIKE '%" + city + "%'", new String[]{category_name});
        return subcategories;
    }

}
