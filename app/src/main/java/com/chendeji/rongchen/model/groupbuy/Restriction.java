package com.chendeji.rongchen.model.groupbuy;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

/**
 * Created by chendeji on 27/5/15.
 */
@Table(name = "Restriction")
public class Restriction extends SugarRecord {

    //是否可退还
    @Ignore
    public static final int REFUNDABLE = 1;
    @Ignore
    public static final int CANT_REFUNDABLE = 0;

    //是否需要预约
    @Ignore
    public static final int RESERVATION_REQUIRED = 1;
    @Ignore
    public static final int RESERVATION_NOT_REQUIRED = 0;

    public int is_reservation_required;

    public int is_refundable;

    public String special_tips;
}
