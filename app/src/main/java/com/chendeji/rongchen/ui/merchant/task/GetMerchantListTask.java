package com.chendeji.rongchen.ui.merchant.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.NetUtil;
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
    public GetMerchantListTask(Context context, UITaskCallBack taskCallBack
            , Sort sort, int page, int limit, Offset_Type offsetType
            , Offset_Type out_offset_type, Platform platform) {
        super(context, taskCallBack, false);
        this.mSort = sort;
        this.mPage = page;
        this.mLimit = limit;
        this.mOffset_type = offsetType;
        this.mOut_offset_type = out_offset_type;
        this.mPlatform = platform;
    }

//    @Override
//    protected void onPostExecute(ReturnMes<List<Merchant>> merchantListReturnMes) {
//        super.onPostExecute(merchantListReturnMes);
//        if (merchantListReturnMes == null){
//            if (NetUtil.hasNetwork(mContext)) {
//                fromNetWorkDataError(errorMsg);
//            } else {
//                fromDBDataError(errorMsg);
//            }
//        }
//    }

    private void getSourceData(ReturnMes<List<Merchant>> listReturnMes) {
        if (AppConst.OK.equals(listReturnMes.status)) {
            List<Merchant> list = listReturnMes.object;
            mTaskCallBack.onPostExecute(list);
        } else {
            ErrorInfo errorInfo = listReturnMes.errorInfo;
            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected void fromDBDataSuccess(ReturnMes<List<Merchant>> listReturnMes) {
        getSourceData(listReturnMes);
    }

    @Override
    protected void fromNetWorkDataSuccess(ReturnMes<List<Merchant>> listReturnMes) {
        getSourceData(listReturnMes);
    }

    @Override
    protected void fromDBDataError(String errorMsg) {
        ToastUtil.showLongToast(mContext, errorMsg);
    }

    @Override
    protected void fromNetWorkDataError(String errorMsg) {
        ToastUtil.showLongToast(mContext, errorMsg);
    }

    @Override
    protected ReturnMes<List<Merchant>> getDataFromNetwork() throws IOException, HttpException {
            AppServerFactory factory = AppServerFactory.getFactory();
            IMerchantOperation operation = factory.getMerchantOperation();
            final ReturnMes<List<Merchant>> merchants = operation.findMerchants(null, null, mSort
                    , mPage, mLimit, mOffset_type, mOut_offset_type, mPlatform);
            //缓存数据
//            SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
//                @Override
//                public void manipulateInTransaction() {
//                    if (merchants != null){
//                        List<Merchant> merchantList = merchants.object;
//                        for (Merchant merchant : merchantList){
//                            merchant.save();
//                        }
//                    }
//                }
//            });
            return merchants;
    }

    @Override
    protected ReturnMes<List<Merchant>> getDataFromDB() {
        //TODO 从数据库中拿到数据
//        SettingFactory factory = SettingFactory.getInstance();
//        String category = factory.getCurrentChoosedCategory();
//        String city = factory.getCurrentCity();
//        List<Merchant> merchants = Merchant.find(Merchant.class, "", new String[]{}, null,
//                "order by distance ASC",
//                "limit mlimit offset mpage");
        return null;
    }

//    @Override
//    protected ReturnMes<List<Merchant>> doInBackground(Void... params) {
//        try {
//            AppServerFactory factory = AppServerFactory.getFactory();
//            IMerchantOperation operation = factory.getMerchantOperation();
//            ReturnMes<List<Merchant>> merchants = operation.findMerchants(null, null, mSort
//                    , mPage, mLimit, mOffset_type, mOut_offset_type, mPlatform);
//            return merchants;
//        } catch (IOException e) {
//            Logger.i(this.getClass().getSimpleName(), "解析错误");
//        } catch (HttpException e) {
//            Logger.i(this.getClass().getSimpleName(), "网络错误");
//        }
//        return null;
//    }
}
