package com.chendeji.rongchen.ui.city;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.StatusBarUtil;
import com.chendeji.rongchen.common.view.CommonSearchLayout;
import com.chendeji.rongchen.ui.city.fragment.HotCityFragment;
import com.chendeji.rongchen.ui.merchant.MerchantListActivity;

public class ChooseCityActivity extends AppCompatActivity implements CommonSearchLayout.OnSearchKeyWordChanged,
        HotCityFragment.OnHotCityClicked {

    private LinearLayout fragmentHolder;
    private CommonSearchLayout searchLayout;
    private HotCityFragment hotCityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.translucentStatusBar(this);

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
    }

    private void initFragment() {
        hotCityFragment = HotCityFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction().add(R.id.ll_fragment_holder, hotCityFragment).commit();
    }

    private void initComponent() {
        fragmentHolder = (LinearLayout) findViewById(R.id.ll_fragment_holder);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(hotCityFragment).commit();
    }

    @Override
    public void onKeyClear() {
        //TODO 在清除内容的时候要变更碎片
        Logger.i("chendeji", "onKeyClear");
        getSupportFragmentManager().beginTransaction().show(hotCityFragment).commit();

    }

    @Override
    public void onCityClicked(String city) {
        Intent intent = new Intent(this, MerchantListActivity.class);
        intent.putExtra(MerchantListActivity.CITY, city);
        startActivity(intent);
    }
}
