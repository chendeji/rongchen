package com.chendeji.rongchen.model;

/**
 * @author chendeji
 * @Description: 坐标系偏移类型
 * @date 27/9/14 下午7:17
 * <p/>
 * ${tags}
 */
public enum Offset_Type {
    DEFUALT(0),GAODE(1),TUBA(2);

    private int value;
    private Offset_Type(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
