package com.chendeji.rongchen.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.view.CommonLoadingProgressView;
import com.chendeji.rongchen.common.view.treeview.model.TreeNode;
import com.chendeji.rongchen.common.view.treeview.view.AndroidTreeView;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.server.AppConst;
import com.chendeji.rongchen.ui.category.common.CategoryTreeItemHolder;
import com.chendeji.rongchen.ui.category.fragment.CategoryListFragment;
import com.chendeji.rongchen.ui.category.task.GetCategoryTreeNodeTask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.chendeji.rongchen.ui.merchant.MerchantListActivity;

import java.util.List;

/**
 * 应该会有三个层级，每个层级的数据使用一个view显示，使用fragment来弄当前的这种效果
 * 主要还是recycleview item的动画
 */
public class DealCategoryActivity extends AppCompatActivity implements TreeNode.TreeNodeClickListener {

    private CommonLoadingProgressView progressView;
    private RelativeLayout treeViewHolder;
    private AndroidTreeView tView;
    private TreeNode root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_category);
        toolbar.setTitle(getString(R.string.category_choose));
        setSupportActionBar(toolbar);

        progressView = (CommonLoadingProgressView) findViewById(R.id.common_loading_layout);
//        addFragment(null, CategoryListFragment.CATEGORY);

        root = TreeNode.root();
        tView = new AndroidTreeView(DealCategoryActivity.this,root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(CategoryTreeItemHolder.class);
        tView.setDefaultNodeClickListener(DealCategoryActivity.this);
        treeViewHolder = (RelativeLayout) findViewById(R.id.ll_category_holder);

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        initData();
    }

    private void showProgress(){
        progressView.show();
    }
    private void hideProgress(){
        progressView.hide();
    }


    @Override
    public void onClick(TreeNode node, Object value) {
        CategoryTreeItemHolder.CategoryTreeItem item = (CategoryTreeItemHolder.CategoryTreeItem) value;
        SettingFactory factory = SettingFactory.getInstance();
        if (node.isLeaf()){
            //叶子节点，就记录最近搜多分类，并跳入到商户列表界面
            factory.setCurrentChoosedCategory(((CategoryTreeItemHolder.CategoryTreeItem) value).title);
            Intent intent = new Intent(this, MerchantListActivity.class);
            startActivity(intent);
        } else if (AppConst.RequestParams.ALL.equals(item.title)){
            //中间如果选择全部，也是直接记录最近分类，跳入到商户列表
            String category = (String) node.getParent().getValue();
            factory.setCurrentChoosedCategory(category);
            Intent intent = new Intent(this, MerchantListActivity.class);
            startActivity(intent);
        }
        //不是叶子节点，就记录
    }

    private void initData() {
        //1，从数据库或者服务端获取数据
        //2，然后将数据填充到树形结构中
        new GetCategoryTreeNodeTask(this, SettingFactory.getInstance().getCurrentCity(),
                new UITaskCallBack<ReturnMes<List<TreeNode>>>() {
            @Override
            public void onPreExecute() {
                showProgress();
            }

            @Override
            public void onPostExecute(ReturnMes<List<TreeNode>> returnMes) {
                hideProgress();
                List<TreeNode> treeNodes = returnMes.object;
                if (treeNodes != null && !treeNodes.isEmpty()){
                    for (TreeNode node : treeNodes){
                        root.addChild(node);
                    }
                }
                treeViewHolder.addView(tView.getView());
            }

            @Override
            public void onNetWorkError() {

            }
        }).excuteProxy((Void[])null);

    }

//    public void addFragment(String category, int category_level) {
//        //当点击一个分类如果还有子分类的话，就添加一个fragment，然后动画跳入到刚刚创建的这个fragment
//        //使用回退键，要考虑到fragment的栈处理
//        CategoryListFragment fragment = CategoryListFragment.newInstance(category, category_level);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.ll_category_fragment_holder, fragment)
//                .addToBackStack(null)
//                .commit();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_deal_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onFragmentInteraction(String category, int category_level, boolean isLastBranch) {
//        //在点击item之后，如果点击全部，直接进入到下个界面
//        //如果在数据库中的搜索到的分类没有子分类了，那么也是直接进入到下一个界面
//        Intent intent = new Intent(this, MerchantListActivity.class);
//        //保存当前的分类选择
//        SettingFactory.getInstance().setCurrentChoosedCategory(category);
//        if (isLastBranch){
//
//        }
//        switch (category_level){
//            case CategoryListFragment.CATEGORY:
//                addFragment(category, CategoryListFragment.SUBCATEGORY);
//                break;
//            case CategoryListFragment.SUBCATEGORY:
//                addFragment(category, CategoryListFragment.SUBCATEGORY1);
//                break;
//            case CategoryListFragment.SUBCATEGORY1:
//                startActivity(intent);
//                break;
//        }
//
//
//    }
}
