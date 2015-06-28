package com.chendeji.rongchen.ui.merchant.task;

import android.content.Context;

import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.model.Platform;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.server.iinterface.IMerchantOperation;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.io.IOException;
import java.util.List;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.model.Sort;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;

/**
 * @author chendeji
 * @Description: 获取商户列表后台操作
 * @date 30/10/14 下午10:17
 * <p/>
 * ${tags}
 */
public class GetMerchantListTask extends BaseUITask<Void, Void, ReturnMes<List<Merchant>>> {

    private String mCity;
    private String mCategory;
    private Sort mSort;
    private int mPage;
    private int mLimit;
    private Offset_Type mOffset_type;
    private Offset_Type mOut_offset_type;
    private Platform mPlatform;

    /**
     * 获取商户列表的后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI回调
     */
    public GetMerchantListTask(Context context, UITaskCallBack taskCallBack, String city, String category
            , Sort sort, int page, int limit, Offset_Type offsetType
            , Offset_Type out_offset_type, Platform platform) {
        super(context, taskCallBack);
        this.mCity = city;
        this.mCategory = category;
        this.mSort = sort;
        this.mPage = page;
        this.mLimit = limit;
        this.mOffset_type = offsetType;
        this.mOut_offset_type = out_offset_type;
        this.mPlatform = platform;
    }

    @Override
    protected void onPostExecute(ReturnMes<List<Merchant>> merchantListReturnMes) {
        super.onPostExecute(merchantListReturnMes);
        if (AppConst.OK.equals(merchantListReturnMes.status)) {
            List<Merchant> list = merchantListReturnMes.object;
            mTaskCallBack.onPostExecute(list);
        } else {
            ErrorInfo errorInfo = merchantListReturnMes.errorInfo;
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
    protected ReturnMes<List<Merchant>> getDataFromNetwork() {
        return null;
    }

    @Override
    protected ReturnMes<List<Merchant>> getDataFromDB() {
        return null;
    }

    @Override
    protected ReturnMes<List<Merchant>> doInBackground(Void... params) {
        try {
            AppServerFactory factory = AppServerFactory.getFactory();
            IMerchantOperation operation = factory.getMerchantOperation();
            ReturnMes<List<Merchant>> merchants = operation.findMerchants(mCity, mCategory, mSort
                    , mPage, mLimit, mOffset_type, mOut_offset_type, mPlatform);
            return merchants;
        } catch (IOException e) {
            Logger.i(this.getClass().getSimpleName(), "解析错误");
        } catch (HttpException e) {
            Logger.i(this.getClass().getSimpleName(), "网络错误");
        }
        return null;
    }
}
