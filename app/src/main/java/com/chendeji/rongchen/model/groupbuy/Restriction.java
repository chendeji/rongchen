package com.chendeji.rongchen.model.groupbuy;

/**
 * Created by chendeji on 27/5/15.
 */
public class Restriction {

    //是否可退还
    public static final int REFUNDABLE = 1;
    public static final int CANT_REFUNDABLE = 0;

    //是否需要预约
    public static final int RESERVATION_REQUIRED = 1;
    public static final int RESERVATION_NOT_REQUIRED = 0;

    public int is_reservation_required;
    public int is_refundable;
    public String special_tips;
}
