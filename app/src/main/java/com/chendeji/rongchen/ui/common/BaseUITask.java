package com.chendeji.rongchen.ui.common;

import com.chendeji.rongchen.common.util.NetUtil;

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

    protected int DB_ERROR_CODE = -1;

    private boolean mIsDBOperation = false;
    protected Context mContext;
    protected UITaskCallBack mTaskCallBack;
    protected String errorMsg;

    /**
     * 基础的UI界面后台线程
     *
     * @param context       上下文句柄
     * @param taskCallBack  UI界面回调
     * @param isDBOperation 是否是数据库操作
     */
    public BaseUITask(Context context, UITaskCallBack<R> taskCallBack, boolean isDBOperation) {
        this.mContext = context;
        this.mTaskCallBack = taskCallBack;
        this.mIsDBOperation = isDBOperation;
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
        if (NetUtil.hasNetwork(mContext)
                && !mIsDBOperation) {
            if (r == null) {
                fromNetWorkDataError(errorMsg);
            } else {
                fromNetWorkDataSuccess(r);
            }
        } else {
            if (r == null) {
                fromDBDataError(errorMsg);
            } else {
                fromDBDataSuccess(r);
            }
        }

    }

    /**
     * 从数据库查找成功之后执行
     *
     * @param r 返回结果
     */
    protected abstract void fromDBDataSuccess(R r);

    /**
     * 从网络获取数据成功之后执行
     *
     * @param r 返回结果
     */
    protected abstract void fromNetWorkDataSuccess(R r);

    /**
     * 从数据库查找数据失败之后（返回的结果为空了，会调用）
     *
     * @param errorMsg 数据库后台操作错误信息反馈
     */
    protected abstract void fromDBDataError(String errorMsg);

    /**
     * 从网络查找数据失败之后（返回的结果为空了，会调用）
     *
     * @param errorMsg 网络后台操作错误信息反馈
     */
    protected abstract void fromNetWorkDataError(String errorMsg);

    /**
     * 在子线程中从网络获取数据
     *
     * @return 网络返回的带有数据的操作结果
     */
    protected abstract R getDataFromNetwork() throws Exception;

    /**
     * 在子线程中从数据库获取数据
     *
     * @return 数据库返回的带有数据的操作结果
     */
    protected abstract R getDataFromDB() throws Exception;

    @Override
    protected R doInBackground(V... params) {
        R r = null;
        try {
            if (NetUtil.hasNetwork(mContext)
                    && !mIsDBOperation) {
                // 从网络获取数据
                r = getDataFromNetwork();
            } else {
                // 从数据库获取数据
                r = getDataFromDB();
                mTaskCallBack.onNetWorkError();
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        return r;
    }

    /**
     * 包装模式，在执行后台任务前先判断是否有网络
     *
     * @param params 传参
     * @return 异步线程
     */
    public final AsyncTask<V, P, R> excuteProxy(V... params) {
        return execute(params);

    }

}
