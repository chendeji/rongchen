package com.chendeji.rongchen.model;

/**
 * @author chendeji
 * @Description:
 * @date 27/9/14 下午3:17
 * <p/>
 * ${tags}
 */
public class ErrorInfo {

    public int errorCode;
    public String errorMsg;

    public ErrorInfo(){}
    public ErrorInfo(int code, String msg){
        this.errorCode = code;
        this.errorMsg = msg;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
