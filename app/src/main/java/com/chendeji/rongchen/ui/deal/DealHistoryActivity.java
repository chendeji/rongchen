package com.chendeji.rongchen.ui.deal;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ToastUtil;
import com.chendeji.rongchen.common.view.CommonProgressDialog;
import com.chendeji.rongchen.common.view.swipy.SwipyRefreshLayout;
import com.chendeji.rongchen.common.view.swipy.SwipyRefreshLayoutDirection;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.chendeji.rongchen.ui.deal.adapter.DealHistoryAdapter;
import com.chendeji.rongchen.ui.deal.task.GetDealDetailInfoTask;
import com.chendeji.rongchen.ui.deal.task.GetDealListFromDB;

import java.util.ArrayList;
import java.util.List;

public class DealHistoryActivity extends AppCompatActivity implements SwipyRefreshLayout.OnRefreshListener {

    private static final int DEFUALT_CURRENTPAGE = 0;
    private SwipyRefreshLayout mRefreshLayout;
    private RecyclerView mDealHistoryList;
    private DealHistoryAdapter mAdapter;
    private List<Deal> deals;
    private int mCurrentPage = DEFUALT_CURRENTPAGE;
    private CommonProgressDialog progressDialog;
    private AsyncTask<Void, Void, ReturnMes<List<Deal>>> getDealListTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_deal_history);
        toolbar.setTitle(getString(R.string.deal_history));
        setSupportActionBar(toolbar);

        initComponent();
        initData();

        synDataFromDB();
    }

    private void showLoadingProgress(){
        if (progressDialog == null) {
            progressDialog = new CommonProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void hideLoadingProgress(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (getDealListTask != null && !getDealListTask.isCancelled()){
            getDealListTask.cancel(true);
            getDealListTask = null;
        }
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        //刷新数据
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            refreshData();
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            loadMoreData();
        }
    }

    private void loadMoreData() {
        mCurrentPage++;
        synDataFromDB();
    }

    private void refreshData() {
        mCurrentPage = DEFUALT_CURRENTPAGE;
        synDataFromDB();
    }

    public void stopRefresh() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void synDataFromDB() {
        getDealListTask = new GetDealListFromDB(this, new UITaskCallBack<ReturnMes<List<Deal>>>() {
            @Override
            public void onPreExecute() {
                showLoadingProgress();
            }

            @Override
            public void onPostExecute(ReturnMes<List<Deal>> returnMes) {
                hideLoadingProgress();
                List<Deal> deals = returnMes.object;
                if (deals != null && !deals.isEmpty()){
                    if (mAdapter != null){
                        if (mCurrentPage > DEFUALT_CURRENTPAGE){
                            //添加
                            mAdapter.addData(deals);
                        } else {
                            //刷新
                            mAdapter.refreshData(deals);
                        }
                    }
                }
                stopRefresh();
            }

            @Override
            public void onExecuteError(String errorMsg) {
                stopRefresh();
                hideLoadingProgress();
                ToastUtil.showLongToast(DealHistoryActivity.this, errorMsg);
            }
        }, mCurrentPage).excuteProxy((Void[]) null);
    }

    private void initData() {
        if (mAdapter == null) {
            deals = new ArrayList<>();
            mAdapter = new DealHistoryAdapter(this, deals);
        }
        mDealHistoryList.setAdapter(mAdapter);
    }

    private void initComponent() {
        mRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.sr_deal_list_reflesh_holder);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.blue, R.color.yellow, R.color.light_red, R.color.green);
        mRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);

        mDealHistoryList = (RecyclerView) findViewById(R.id.rv_deal_history_list);
        mDealHistoryList.setLayoutManager(new LinearLayoutManager(this));
        mDealHistoryList.addItemDecoration(new DealHistoryItemDecoration(10));
    }

    class DealHistoryItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public DealHistoryItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_deal_history, menu);
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
}
