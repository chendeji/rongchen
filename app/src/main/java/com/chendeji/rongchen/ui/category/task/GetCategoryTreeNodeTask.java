package com.chendeji.rongchen.ui.category.task;

import android.content.Context;

import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.view.treeview.model.TreeNode;
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
        super(context, taskCallBack);
        this.mCity = city;
    }

    @Override
    protected void onPostExecute(ReturnMes<List<TreeNode>> listReturnMes) {
        super.onPostExecute(listReturnMes);
        if (AppConst.OK.equals(listReturnMes.status)) {
            mTaskCallBack.onPostExecute(listReturnMes);
        } else {
//            ErrorInfo errorInfo = listReturnMes.errorInfo;
//            ToastUtil.showLongToast(mContext, errorInfo.toString());
        }
    }

    @Override
    protected ReturnMes<List<TreeNode>> doInBackground(Void... params) {

        //先获取数据
        //编写后台访问接口
        List<TreeNode> treeNodes = new ArrayList<>();
        ReturnMes<List<TreeNode>> treeReturnMes = new ReturnMes<>();
        try {
            ReturnMes<List<Category>> returnMes;
            List<Category> categories = Category.find(Category.class, null, new String[]{});
            if (categories == null || categories.size() == 0) {
                returnMes = AppServerFactory.getFactory().getCategoryOperation().getCategory("");
                //获取数据之后将数据填充到数据库
                if (returnMes != null) {
                    categories = returnMes.object;
                    final List<Category> finalCategories = categories;
                    SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                        @Override
                        public void manipulateInTransaction() {
                            for (Category category : finalCategories) {
                                category.save();
                            }
                        }
                    });
                }
            }

            TreeNode categoryNode;
            TreeNode subCategoryNode;
            TreeNode subCategroy1Node;
            if (categories != null && !categories.isEmpty()) {
                //1，开始填充treenode数据
                for (Category category : categories) {
                    categoryNode = new TreeNode(new CategoryTreeItemHolder.CategoryTreeItem(0, category.category_name));
                    categoryNode.addChild(new TreeNode(new CategoryTreeItemHolder.CategoryTreeItem(0, AppConst.RequestParams.ALL)));
                    List<SubCategory> subCategories = category.getSubcategories();
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

            if (!treeNodes.isEmpty()) {
                treeReturnMes.status = AppConst.OK;
                treeReturnMes.object = treeNodes;
                return treeReturnMes;
            }

        } catch (IOException e) {
            Logger.i(this.getClass().getSimpleName(), "解析错误");
        } catch (HttpException e) {
            Logger.i(this.getClass().getSimpleName(), "网络错误");
        }


        return treeReturnMes;
    }
}
