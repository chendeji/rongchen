package com.chendeji.rongchen.model;

/**
 * @author chendeji
 * @Description: 平台类型，使用默认值为HTML5
 * @date 27/9/14 下午7:22
 * <p/>
 * ${tags}
 */
public enum Platform {

    WEB(1),HTML5(2);
    private int value;
    private Platform(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
    public int getDefualt(){
        return HTML5.getValue();
    }
}
