package com.chendeji.rongchen.ui.city;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.SettingFactory;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.StatusBarUtil;
import com.chendeji.rongchen.common.view.CommonLoadingProgressView;
import com.chendeji.rongchen.common.view.CommonSearchLayout;
import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.ui.category.DealCategoryActivity;
import com.chendeji.rongchen.ui.city.fragment.CitiesFragment;
import com.chendeji.rongchen.ui.city.fragment.HotCityFragment;
import com.chendeji.rongchen.ui.city.task.CitySearchTask;
import com.chendeji.rongchen.ui.common.UITaskCallBack;
import com.chendeji.rongchen.ui.merchant.MerchantListActivity;

import java.util.List;

public class ChooseCityActivity extends AppCompatActivity implements CommonSearchLayout.OnSearchKeyWordChanged,
        HotCityFragment.OnHotCityClicked, CitiesFragment.OnCityListItemClickedListener {

    private CommonSearchLayout searchLayout;
    private HotCityFragment hotCityFragment;
    private CitiesFragment citiesFragment;
    private AsyncTask<Void, Void, ReturnMes<List<String>>> citySearchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.translucentStatusBar(this);

        //1，通过访问接口获取最新的支持团购商户搜索的城市列表
        //2，第一次加载就显示加载进度条，后面默认使用数据库缓存了。
        //3，右侧有一条城市头字母导航栏用于快速定位城市 不过貌似不需要
        //4，
        setContentView(R.layout.activity_choose_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_choose_city);
        toolbar.setTitle(getString(R.string.choose_city));
        setSupportActionBar(toolbar);

        initComponent();
        initFragment();
        initData();
    }

    private void initData() {
        //更新应用的城市数据库信息
        citySearchTask = new CitySearchTask(this, new UITaskCallBack<ReturnMes<List<String>>>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(ReturnMes<List<String>> returnMes) {

            }

            @Override
            public void onNetWorkError() {

            }
        }).excuteProxy((Void[]) null);
    }

    private void initFragment() {
        hotCityFragment = HotCityFragment.newInstance(null, null);
        citiesFragment = CitiesFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll_fragment_holder, hotCityFragment)
                .add(R.id.ll_fragment_holder, citiesFragment)
                .hide(citiesFragment).show(hotCityFragment)
                .commit();
    }

    private void initComponent() {
        searchLayout = (CommonSearchLayout) findViewById(R.id.sl_search_city);
        searchLayout.setKeyWordChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_choose_city, menu);
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

    @Override
    public void onKeyChanged(String keyWord) {
        //TODO 搜索关键字变更的时候，要刷新碎片中列表的数据
        Logger.i("chendeji", "onKeyChanged");
        getSupportFragmentManager().beginTransaction()
                .hide(hotCityFragment)
                .show(citiesFragment)
                .commit();
        citiesFragment.searchKeyChanged(keyWord);
    }

    @Override
    public void onKeyClear() {
        //TODO 在清除内容的时候要变更碎片
        Logger.i("chendeji", "onKeyClear");
        getSupportFragmentManager().beginTransaction()
                .show(hotCityFragment)
                .hide(citiesFragment)
                .commit();

    }

    @Override
    public void onCityClicked(String city) {
        goChooseCategory(city);
    }

    private void goChooseCategory(String city){
        SettingFactory factory = SettingFactory.getInstance();
        factory.setCurrentCity(city);
        factory.addRecentSearchCity(city);

        Intent intent = new Intent(this, DealCategoryActivity.class);
        intent.putExtra(MerchantListActivity.CITY, city);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (hotCityFragment != null){
            hotCityFragment = null;
        }
        if (citiesFragment != null){
            citiesFragment = null;
        }
        cancelTask();
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(String city) {
        goChooseCategory(city);
    }

    private void cancelTask(){
        if (citySearchTask != null){
            if (!citySearchTask.isCancelled()){
                citySearchTask.cancel(true);
                citySearchTask = null;
            }
        }
    }
}
