package com.chendeji.rongchen.ui.deal.task;

import android.content.Context;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.dao.tables.deal.DealTable;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.util.List;

/**
 * Created by chendeji on 9/7/15.
 */
public class GetDealListFromDB extends BaseUITask<Void, Void, ReturnMes<List<Deal>>> {
    private int mPage;
    private int mLimit = 5;

    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public GetDealListFromDB(Context context, UITaskCallBack<ReturnMes<List<Deal>>> taskCallBack, int page) {
        super(context, taskCallBack, true);
        this.mPage = page;
    }

    @Override
    protected void fromDBDataSuccess(ReturnMes<List<Deal>> listReturnMes) {
        if (AppConst.OK.equalsIgnoreCase(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        }
    }

    @Override
    protected void fromNetWorkDataSuccess(ReturnMes<List<Deal>> listReturnMes) {

    }

    @Override
    protected void fromDBDataError(String errorMsg) {

    }

    @Override
    protected void fromNetWorkDataError(String errorMsg) {

    }

    @Override
    protected ReturnMes<List<Deal>> getDataFromNetwork() throws Exception {
        return null;
    }

    @Override
    protected ReturnMes<List<Deal>> getDataFromDB() throws Exception {
        ReturnMes<List<Deal>> returnMes = null;
        String orderBy = DealTable.SAVINGTIME + " DESC ";
//        String limit = " limit " + mPage * mLimit + "," + mLimit;
        List<Deal> deals = Deal.find(Deal.class, null, new String[]{}, null, orderBy, null);
        if (deals != null && !deals.isEmpty()) {
            returnMes = new ReturnMes<>();
            returnMes.status = AppConst.OK;
            returnMes.object = deals;
        } else {
            errorMsg = mContext.getString(R.string.no_more_data);
        }
        return returnMes;
    }
}
