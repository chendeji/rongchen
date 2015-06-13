package com.chendeji.rongchen.ui.merchant.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.io.IOException;

/**
 * Created by chendeji on 31/5/15.
 */
public class GetMerchantDetailInfoTask extends BaseUITask<Void, Void, ReturnMes<Merchant>> {

    private final long mMerchantID;

    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public GetMerchantDetailInfoTask(Context context, UITaskCallBack<ReturnMes<Merchant>> taskCallBack, long merchant_id) {
        super(context, taskCallBack);
        this.mMerchantID = merchant_id;
    }

    @Override
    protected void onPostExecute(ReturnMes<Merchant> merchantReturnMes) {
        super.onPostExecute(merchantReturnMes);
        if (AppConst.OK.equals(merchantReturnMes.status)){
            mTaskCallBack.onPostExecute(merchantReturnMes);
        }else{
            ErrorInfo errorInfo = merchantReturnMes.errorInfo;
            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected ReturnMes<Merchant> doInBackground(Void... params) {

        ReturnMes<Merchant> merchantReturnMes = null;
        try {
            merchantReturnMes = AppServerFactory.getFactory().getMerchantOperation().findSingleMerchant(mMerchantID);
        } catch (IOException e) {
            Logger.i(this.getClass().getSimpleName(), "解析错误");
        } catch (HttpException e) {
            Logger.i(this.getClass().getSimpleName(), "网络错误");
        }
        return merchantReturnMes;
    }
}
