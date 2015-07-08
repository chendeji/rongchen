package com.chendeji.rongchen.ui.category.task;

import android.content.Context;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.view.treeview.model.TreeNode;
import com.chendeji.rongchen.dao.tables.category.SubCategoryTable;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.category.Category;
import com.chendeji.rongchen.model.category.SubCategory;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.server.AppServerFactory;
import com.chendeji.rongchen.server.HttpException;
import com.chendeji.rongchen.ui.category.common.CategoryTreeItemHolder;
import com.chendeji.rongchen.ui.common.BaseUITask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.orm.SugarTransactionHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 26/6/15.
 */
public class GetCategoryTreeNodeTask extends BaseUITask<Void, Void, ReturnMes<List<TreeNode>>> {

    private final String mCity;

    /**
     * 基础的UI界面后台线程
     *
     * @param context      上下文句柄
     * @param taskCallBack UI界面回调
     */
    public GetCategoryTreeNodeTask(Context context, String city, UITaskCallBack<ReturnMes<List<TreeNode>>> taskCallBack) {
        super(context, taskCallBack, false);
        this.mCity = city;
    }

//    @Override
//    protected void onPostExecute(ReturnMes<List<TreeNode>> listReturnMes) {
//        super.onPostExecute(listReturnMes);
//    }

    @Override
    protected void fromDBDataSuccess(ReturnMes<List<TreeNode>> listReturnMes) {
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
//            ErrorInfo errorInfo = listReturnMes.errorInfo;
//            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected void fromNetWorkDataSuccess(ReturnMes<List<TreeNode>> listReturnMes) {
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
//            ErrorInfo errorInfo = listReturnMes.errorInfo;
//            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected void fromDBDataError(String errorMsg) {

    }

    @Override
    protected void fromNetWorkDataError(String errorMsg) {

    }

    @Override
    protected ReturnMes<List<TreeNode>> getDataFromNetwork() throws IOException, HttpException {
        List<TreeNode> treeNodes = new ArrayList<>();
        ReturnMes<List<TreeNode>> treeReturnMes = new ReturnMes<>();
        final String currentCity = SettingFactory.getInstance().getCurrentCity();
        ReturnMes<List<Category>> returnMes;
        List<Category> categories = null;
        returnMes = AppServerFactory.getFactory().getCategoryOperation().getCategory(currentCity);
        //获取数据之后将数据填充到数据库
        if (returnMes != null) {
            categories = returnMes.object;
            if (categories != null && !categories.isEmpty()) {
                final List<Category> finalCategories = categories;
                SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                    @Override
                    public void manipulateInTransaction() {
                        for (Category category : finalCategories) {
                            List<SubCategory> subCategories = category.getSubcategories();
                            for (SubCategory subCategory : subCategories) {
                                subCategory.addBelongCity(currentCity);
                                subCategory.save();
                            }
                            category.save();
                        }
                    }
                });
            } else {
                errorMsg = mContext.getString(R.string.data_no_prepare_please_wait);
                return null;
            }

        } else {
            errorMsg = mContext.getString(R.string.data_no_prepare_please_wait);
            return null;
        }
        //数据获取之后进行填充
        getCategoryTreeNodes(treeNodes, categories, currentCity);
        if (treeNodes != null && !treeNodes.isEmpty()) {
            treeReturnMes.status = returnMes.status;
            treeReturnMes.object = treeNodes;
            return treeReturnMes;
        } else {
            errorMsg = mContext.getString(R.string.data_no_prepare_please_wait);
            return null;
        }
    }

    @Override
    protected ReturnMes<List<TreeNode>> getDataFromDB() {
        List<TreeNode> treeNodes = new ArrayList<>();
        ReturnMes<List<TreeNode>> treeReturnMes = new ReturnMes<>();
        String city = SettingFactory.getInstance().getCurrentCity();
        //1，需要先判断一下数据库中是否有该城市相关的分类数据
        List<SubCategory> subCategories = SubCategory.find(SubCategory.class, SubCategoryTable.STR_CITIES + "LIKE '%" + city + "%'", new String[]{});
        if (subCategories == null || subCategories.isEmpty()) {
            return null;
        }

        List<Category> categories = Category.find(Category.class, null, new String[]{});

        if (categories != null && !categories.isEmpty()) {
            getCategoryTreeNodes(treeNodes, categories, city);
            if (!treeNodes.isEmpty()) {
                treeReturnMes.status = AppConst.OK;
                treeReturnMes.object = treeNodes;
                return treeReturnMes;
            }
        }

        return null;
    }

    private void getCategoryTreeNodes(List<TreeNode> treeNodes, List<Category> categories, String city) {
        TreeNode categoryNode;
        TreeNode subCategoryNode;
        TreeNode subCategroy1Node;
        if (categories != null && !categories.isEmpty()) {
            //1，开始填充treenode数据
            for (Category category : categories) {
                categoryNode = new TreeNode(new CategoryTreeItemHolder.CategoryTreeItem(0, category.category_name));
                categoryNode.addChild(new TreeNode(new CategoryTreeItemHolder.CategoryTreeItem(0, AppConst.RequestParams.ALL)));
                List<SubCategory> subCategories = category.getSubCategoryByCity(city);
                if (subCategories != null && !subCategories.isEmpty()) {
                    for (SubCategory subCategory : subCategories) {
                        subCategoryNode = new TreeNode(new CategoryTreeItemHolder.CategoryTreeItem(0, subCategory.category_name));
                        subCategoryNode.addChild(new TreeNode(new CategoryTreeItemHolder.CategoryTreeItem(0, AppConst.RequestParams.ALL)));
                        List<String> subcategory1 = subCategory.getSubcategories();
                        if (subcategory1 != null && !subcategory1.isEmpty()) {
                            for (String subcategories1 : subcategory1) {
                                subCategroy1Node = new TreeNode(new CategoryTreeItemHolder.CategoryTreeItem(0, subcategories1));
                                subCategoryNode.addChild(subCategroy1Node);
                            }
                        }
                        categoryNode.addChildren(subCategoryNode);
                    }
                }
                treeNodes.add(categoryNode);
            }
        }
    }
}
