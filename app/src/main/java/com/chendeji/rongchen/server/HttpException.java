package com.chendeji.rongchen.server;

/**
 * @author chendeji
 * @Description:
 * @date 29/9/14 下午11:19
 * <p/>
 * ${tags}
 */
public class HttpException extends Exception {

    public int retCode;
    public String retMsg;

    public HttpException(){}
    public HttpException(int code, String msg){
        this.retCode = code;
        this.retMsg = msg;
    }
}
