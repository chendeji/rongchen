package com.chendeji.rongchen.ui.deal.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.dao.tables.deal.DealTable;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.io.IOException;
import java.util.List;

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
        super(context, taskCallBack, false);
        this.mDeal_id = deal_id;
    }

//    @Override
//    protected void onPostExecute(ReturnMes<Deal> dealReturnMes) {
//        super.onPostExecute(dealReturnMes);
//    }

    @Override
    protected void fromDBDataSuccess(ReturnMes<Deal> dealReturnMes) {

    }

    @Override
    protected void fromNetWorkDataSuccess(ReturnMes<Deal> dealReturnMes) {
        if (AppConst.OK.equals(dealReturnMes.status)) {
            mTaskCallBack.onPostExecute(dealReturnMes);
        } else {
            ErrorInfo errorInfo = dealReturnMes.errorInfo;
            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected void fromDBDataError(String errorMsg) {

    }

    @Override
    protected void fromNetWorkDataError(String errorMsg) {

    }

    @Override
    protected ReturnMes<Deal> getDataFromNetwork() throws IOException, HttpException {
        ReturnMes<Deal> dealReturnMes = AppServerFactory.getFactory().getDealOperation().getDeal(mDeal_id);
        if (dealReturnMes != null && dealReturnMes.status.equalsIgnoreCase(AppConst.OK)) {
            Deal deal = dealReturnMes.object;
            //将这个deal缓存到数据库
            if (deal != null) {
                deal.savingtime = System.currentTimeMillis() / 1000;
                deal.save();
            }
        }
        return dealReturnMes;
    }

    @Override
    protected ReturnMes<Deal> getDataFromDB() {
        return null;
    }
}
