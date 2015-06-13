package com.chendeji.rongchen.ui.common;

/**
 * @author chendeji
 * @Description: UI界面回调接口
 * @date 30/10/14 下午10:20
 * <p/>
 * ${tags}
 */
public interface UITaskCallBack<T> {

    /**
     * 后台操作执行前
     */
    void onPreExecute();

    /**
     * 后台操作结束后
     * @param returnMes 服务端返回的信息
     */
    void onPostExecute(T returnMes);

    /**
     * 没有网络情况的操作
     */
    void onNetWorkError();

}
