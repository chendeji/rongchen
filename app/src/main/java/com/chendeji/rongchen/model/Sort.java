package com.chendeji.rongchen.model;

/**
 * @author chendeji
 * @Description: 排序类型
 * @date 27/9/14 下午7:04
 * <p/>
 * ${tags}
 */
public enum Sort {

    NONE(0),DEFAULT(1),STARTS_FIRST(2),PRODUCT_EVALUTION(3),ENVIROMENT_EVALUTION(4),SERVICE_EVALUTION(5),
    COMMONTS_NUMBER_FIRST(6),DISTANCE_FIRST(7),AVG_PRICE_LOWER_FIRST(8),AVG_PRICE_HIGHER_FIRST(9);

    private int value;
    private Sort(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }

}
