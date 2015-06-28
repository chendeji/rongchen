package com.chendeji.rongchen.ui.common;

import com.chendeji.rongchen.common.util.NetUtil;
import com.chendeji.rongchen.common.util.ToastUtil;

import android.content.Context;
import android.os.*;

/**
 * @author chendeji
 * @Description: 基本的UI后台线程
 * @date 30/10/14 下午10:18
 * <p/>
 * ${tags}
 */
public abstract class BaseUITask<V, P, R> extends AsyncTask<V, P, R> {
    protected Context mContext;
    protected UITaskCallBack mTaskCallBack;

    /**
     * 基础的UI界面后台线程
     * @param context 上下文句柄
     * @param taskCallBack UI界面回调
     */
    public BaseUITask (Context context, UITaskCallBack<R> taskCallBack){
        this.mContext = context;
        this.mTaskCallBack = taskCallBack;
    }

    @Override
    protected void onPreExecute() {
        mTaskCallBack.onPreExecute();
    }

    @Override
    protected void onPostExecute(R r) {
        if (mContext == null)
            return;
        //判断返回的服务端数据是否有错误
        if (r == null){
            if (NetUtil.hasNetwork(mContext)) {
                fromNetWorkDataError();
            } else {
                fromDBDataError();
            }
            return;
        }
    }

    /**
     * 从数据库查找数据失败之后（返回的结果为空了，会调用）
     */
    protected abstract void fromDBDataError();

    /**
     * 从网络查找数据失败之后（返回的结果为空了，会调用）
     */
    protected abstract void fromNetWorkDataError();

    /**
     * 在子线程中从网络获取数据
     * @return
     */
    protected abstract R getDataFromNetwork();

    /**
     * 在子线程中从数据库获取数据
     * @return
     */
    protected abstract R getDataFromDB();

    @Override
    protected R doInBackground(V... params) {
        R r = null;
        if (NetUtil.hasNetwork(mContext)) {
            // 从网络获取数据
            r = getDataFromNetwork();
        } else {
            // 从数据库获取数据
            r = getDataFromDB();
            mTaskCallBack.onNetWorkError();
        }
        return r;
    }

    /**
     * 包装模式，在执行后台任务前先判断是否有网络
     * @param params 传参
     * @return 异步线程
     */
    public final AsyncTask<V,P,R> excuteProxy(V ...params){
        return execute(params);

    }

}
