package com.chendeji.rongchen.ui.category.task;

import android.content.Context;

import com.chendeji.rongchen.dao.tables.category.SubCategoryTable;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.category.SubCategory;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 25/6/15.
 */
public class GetCategoryFromDBTask extends BaseUITask<Void, Void, ReturnMes<List<String>>> {

    public static final int CATEGORY = 0;
    public static final int SUBCATEGORY = 1;
    public static final int SUBCATEGORY1 = 2;

    private final String mCategory;
    private final int mCategoryLevel;

    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public GetCategoryFromDBTask(Context context, String category, int category_level, UITaskCallBack<ReturnMes<List<String>>> taskCallBack) {
        super(context, taskCallBack);
        this.mCategory = category;
        this.mCategoryLevel = category_level;
    }

    @Override
    protected void onPostExecute(ReturnMes<List<String>> listReturnMes) {
        super.onPostExecute(listReturnMes);
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
//            ErrorInfo errorInfo = listReturnMes.errorInfo;
//            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected ReturnMes<List<String>> doInBackground(Void... params) {
        ReturnMes<List<String>> returnMes = new ReturnMes<>();
        returnMes.status = AppConst.ERROR;
        List<SubCategory> subCategories;
        List<String> categoryList;
        if (mCategoryLevel == CATEGORY) {
            subCategories = SubCategory.find(SubCategory.class,
                    SubCategoryTable.PARENT_CATEGORY_NAME + "=?", new String[]{mCategory});

            if (subCategories != null && !subCategories.isEmpty()) {
                categoryList = new ArrayList<>();
                for (SubCategory subCategory : subCategories) {
                    categoryList.add(subCategory.category_name);
                }
                returnMes.status = AppConst.OK;
                returnMes.object = categoryList;
                return returnMes;
            } else{
                returnMes.status = AppConst.ERROR;

            }
            return returnMes;
        } else if (mCategoryLevel == SUBCATEGORY) {
            subCategories = SubCategory.find(SubCategory.class,
                    SubCategoryTable.CATEGORY_NAME + "=?", new String[]{mCategory});
            if (subCategories != null && !subCategories.isEmpty()) {
                SubCategory subCategory = subCategories.get(0);
                if (subCategory != null) {
                    categoryList = subCategory.getSubcategories();
                    returnMes.status = AppConst.OK;
                    returnMes.object = categoryList;
                }
            } else {
                returnMes.status = AppConst.ERROR;

            }
            return returnMes;
        }
        return null;
    }
}
