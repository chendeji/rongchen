package com.chendeji.rongchen.ui.deal.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.io.IOException;

/**
 * Created by chendeji on 27/5/15.
 */
public class GetDealDetailInfoTask extends BaseUITask<Void, Void, ReturnMes<Deal>> {


    private final String mDeal_id;

    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public GetDealDetailInfoTask(Context context, UITaskCallBack taskCallBack, String deal_id) {
        super(context, taskCallBack);
        this.mDeal_id = deal_id;
    }

    @Override
    protected ReturnMes<Deal> doInBackground(Void... params) {
        try {
            ReturnMes<Deal> dealReturnMes = AppServerFactory.getFactory().getDealOperation().getDeal(mDeal_id);
            return dealReturnMes;
        } catch (IOException e) {
            Logger.i(this.getClass().getSimpleName(), "解析错误");
        } catch (HttpException e) {
            Logger.i(this.getClass().getSimpleName(), "网络错误");
        }
        return null;
    }

    @Override
    protected void onPostExecute(ReturnMes<Deal> dealReturnMes) {
        super.onPostExecute(dealReturnMes);
        if (AppConst.OK.equals(dealReturnMes.status)) {
            mTaskCallBack.onPostExecute(dealReturnMes);
        } else {
            ErrorInfo errorInfo = dealReturnMes.errorInfo;
            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected void fromDBDataError() {

    }

    @Override
    protected void fromNetWorkDataError() {

    }

    @Override
    protected ReturnMes<Deal> getDataFromNetwork() {
        return null;
    }

    @Override
    protected ReturnMes<Deal> getDataFromDB() {
        return null;
    }
}
