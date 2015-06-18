package com.chendeji.rongchen.ui.map;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.WalkRouteResult;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.common.util.Logger;
import com.chendeji.rongchen.common.util.StatusBarUtil;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.map.adapter.RoutePagerAdapter;
import com.chendeji.rongchen.ui.map.view.CommonRouteListView;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, IMap.OnSearchRouteListener {

    private static final String TAG = RouteActivity.class.getSimpleName();

    public static final int REQUEST_KEY = 100;
    private int mRouteType;

    public static final String END_POINT_LOCATION_KEY = "end_point_location";
    public static final String ROUTE_TYPE = "route_type";
    public static final String Route = "route_result";
    private Toolbar routeToolBar;
    private TextView endAddress;
    private ViewPager routeViewPager;
    private RadioGroup route_group;

    private CommonRouteListView busRoutes;     //公交车路线
    private CommonRouteListView carRoutes;     //汽车路线
    private CommonRouteListView walkRoutes;    //步行路线
    private int route_type;

    private double[] end_point_location;
    private double[] start_point_location;
    private Merchant merchant;
    private IMap iMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.translucentStatusBar(this);
        setContentView(R.layout.activity_road_line_planning);

        iMap = MapManager.getManager().getMap();
        start_point_location = iMap.getLocation();
        route_type = getIntent().getIntExtra(RouteActivity.ROUTE_TYPE, -1);
        merchant = (Merchant) getIntent().getSerializableExtra(RouteActivity.END_POINT_LOCATION_KEY);
        end_point_location = new double[]{merchant.latitude, merchant.longitude};

        routeToolBar = (Toolbar) findViewById(R.id.tb_route);
        routeToolBar.setTitle(getString(R.string.check_route));
        setSupportActionBar(routeToolBar);

        initComponent();
        initEvent();

        setData();
        startRoute();
    }

    private void startRoute() {
        iMap.startRoute(this, start_point_location, end_point_location, route_type);
        switch (route_type){
            case IMap.BUS_ROUTE:
                busRoutes.showLoading();
                break;
            case IMap.CAR_ROUTE:
                carRoutes.showLoading();
                break;
            case IMap.WALK_ROUTE:
                walkRoutes.showLoading();
                break;
        }
    }

    private void setData() {
        endAddress.setText(merchant.address);
    }

    private void initEvent() {
        route_group.setOnCheckedChangeListener(this);
        routeViewPager.addOnPageChangeListener(this);
        iMap.setOnSearchRouteListener(this);
    }

    private void initComponent() {
        //商户地址
        endAddress = (TextView) findViewById(R.id.tv_end_address);
        //三种类型的路线规划
        routeViewPager = (ViewPager) findViewById(R.id.vp_route);
        route_group = (RadioGroup) findViewById(R.id.rg_route_type_group);

        List<View> views = new ArrayList<>();
        if (busRoutes == null){
            busRoutes = new CommonRouteListView(this, null, IMap.BUS_ROUTE);
            views.add(busRoutes);
        }
        if (carRoutes == null){
            carRoutes = new CommonRouteListView(this, null, IMap.CAR_ROUTE);
            views.add(carRoutes);
        }
        if (walkRoutes == null){
            walkRoutes = new CommonRouteListView(this, null, IMap.WALK_ROUTE);
            views.add(walkRoutes);
        }

        routeViewPager.setAdapter(new RoutePagerAdapter(this, views));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_road_line_planning, menu);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 切换viewpager
        switch (checkedId){
            case R.id.rt_bus_button:
                routeViewPager.setCurrentItem(0, true);
                if (busRoutes.getRouteCount() == 0) {
                    iMap.startRoute(this, start_point_location, end_point_location, IMap.BUS_ROUTE);
                }
                break;
            case R.id.rt_car_button:
                routeViewPager.setCurrentItem(1, true);
                if (carRoutes.getRouteCount() == 0) {
                    iMap.startRoute(this, start_point_location, end_point_location, IMap.CAR_ROUTE);
                    carRoutes.showLoading();
                }
                break;
            case R.id.rt_walk_button:
                routeViewPager.setCurrentItem(2, true);
                if (walkRoutes.getRouteCount() == 0) {
                    iMap.startRoute(this, start_point_location, end_point_location, IMap.WALK_ROUTE);
                    walkRoutes.showLoading();
                }
                break;
        }
        // 加载路径规划

    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBeginSearch() {
        //TODO 在加载路径规划开始的时候做的事情
        //显示加载进度条对话框
        Logger.d(TAG, "onBeginSearch");
    }

    @Override
    public void onSearcheDone(int routeType, RouteResult result) {
        Logger.d(TAG, "onSearchDone");
        //关闭进度条对话框
        if (result == null){
            // 提示数据错误
        }
        // 显示数据
        switch (routeType){
            case IMap.BUS_ROUTE:
                BusRouteResult busRouteResult = (BusRouteResult) result;
                setBusRroute(busRouteResult);
                break;
            case IMap.CAR_ROUTE:
                DriveRouteResult driveRouteResult = (DriveRouteResult) result;
                setDriveRoute(driveRouteResult);
                break;
            case IMap.WALK_ROUTE:
                WalkRouteResult walkRouteResult = (WalkRouteResult) result;
                setWalkRoute(walkRouteResult);
                break;
        }
    }

    private void setWalkRoute(WalkRouteResult walkRouteResult) {
        walkRoutes.setRouteResult(walkRouteResult);
    }

    private void setDriveRoute(DriveRouteResult driveRouteResult) {
        carRoutes.setRouteResult(driveRouteResult);
    }

    private void setBusRroute(BusRouteResult busRouteResult) {
        busRoutes.setRouteResult(busRouteResult);
    }


}
