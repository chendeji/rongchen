package com.chendeji.rongchen.model.city;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

/**
 * Created by chendeji on 22/6/15.
 */
@Table(name = "City")
public class City extends SugarRecord{
    @Column(name = "city",unique = true, notNull = true)
    public String city;

    @Column(name = "FIRSTSPELL")
    public String firstSpell;

    @Column(name = "FULLSPELL")
    public String fullSpell;
}
