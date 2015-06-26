package com.chendeji.rongchen.model.city;

import com.chendeji.rongchen.dao.tables.city.RecentSearchCityTable;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

/**
 * Created by chendeji on 25/6/15.
 */
@Table(name = "RecentSearchCity")
public class RecentSearchCity extends SugarRecord {

    @Column(name = RecentSearchCityTable.CITY_NAME, unique = true, notNull = true)
    public String city_name;

    public int search_count;

}
