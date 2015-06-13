package com.chendeji.rongchen.model;

/**
 * @author chendeji
 * @Description:
 * @date 27/9/14 下午3:17
 * <p/>
 * ${tags}
 */
public class ReturnMes<T> {

    /**
     * 服务端返回数据时返回的状态
     */
    public String status;
    /**
     * 服务端返回数据时出错返回的错误信息
     */
    public ErrorInfo errorInfo;

    public int current_count;

    public T object;

    public Object moreInfo;

    public ReturnMes(){}
    public ReturnMes(int retCode, String msg){
        this.errorInfo = new ErrorInfo(retCode, msg);
    }
}
