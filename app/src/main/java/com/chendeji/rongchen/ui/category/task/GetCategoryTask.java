package com.chendeji.rongchen.ui.category.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.model.ErrorInfo;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.category.Category;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.orm.SugarTransactionHelper;

import java.io.IOException;
import java.util.List;

/**
 * Created by chendeji on 24/6/15.
 */
public class GetCategoryTask extends BaseUITask<Void, Void, ReturnMes<List<Category>>> {


    private final String mCity;

    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public GetCategoryTask(Context context, String city, UITaskCallBack<ReturnMes<List<Category>>> taskCallBack) {
        super(context, taskCallBack);
        this.mCity = city;
    }

    @Override
    protected void onPostExecute(ReturnMes<List<Category>> listReturnMes) {
        super.onPostExecute(listReturnMes);
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
            ErrorInfo errorInfo = listReturnMes.errorInfo;
            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected ReturnMes<List<Category>> doInBackground(Void... params) {
        //编写后台访问接口
        try {
            ReturnMes<List<Category>> returnMes;
            List<Category> categories = Category.find(Category.class, null, new String[]{});
            if (categories == null || categories.size() == 0){
                returnMes = AppServerFactory.getFactory().getCategoryOperation().getCategory("");
                //获取数据之后将数据填充到数据库
                if (returnMes != null) {
                    categories = returnMes.object;
                    final List<Category> finalCategories = categories;
                    SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                        @Override
                        public void manipulateInTransaction() {
                            for (Category category : finalCategories){
                                category.save();
                            }
                        }
                    });
                }
            } else {
                returnMes = new ReturnMes<>();
                returnMes.status = AppConst.OK;
                returnMes.object = categories;
            }
            return returnMes;
        } catch (IOException e) {
            Logger.i(this.getClass().getSimpleName(), "解析错误");
        } catch (HttpException e) {
            Logger.i(this.getClass().getSimpleName(), "网络错误");
        }

        return null;
    }
}
