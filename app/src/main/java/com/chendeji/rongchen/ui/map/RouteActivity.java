package com.chendeji.rongchen.ui.map;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.map.IMap;
import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.rey.material.widget.ListView;
import com.rey.material.widget.TextView;

public class RouteActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    public static final int REQUEST_KEY = 100;
    private int mRouteType;
    public static final int CAR_ROUTE = 0;
    public static final int BUS_ROUTE = 1;
    public static final int WALK_ROUTE = 2;

    public static final String END_POINT_LOCATION_KEY = "end_point_location";
    public static final String ROUTE_TYPE = "route_type";
    public static final String Route = "route_result";
    private Toolbar routeToolBar;
    private TextView endAddress;
    private ViewPager routeViewPager;
    private RadioGroup route_group;

    private ListView busRoutes;     //公交车路线
    private ListView carRoutes;     //汽车路线
    private ListView walkRoutes;    //步行路线
    private int route_type;

    private double[] end_point_location;
    private double[] start_point_location;
    private Merchant merchant;
    private IMap iMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_line_planning);

        iMap = MapManager.getManager().getMap();
        start_point_location = iMap.getLocation();
        route_type = getIntent().getIntExtra(RouteActivity.ROUTE_TYPE, -1);
        merchant = (Merchant) getIntent().getSerializableExtra(RouteActivity.END_POINT_LOCATION_KEY);
        end_point_location = new double[]{merchant.latitude, merchant.longitude};

        routeToolBar = (Toolbar) findViewById(R.id.tb_route);
        routeToolBar.setTitle(getString(R.string.check_route));

        initComponent();
        initEvent();

        setData();
        startRoute();
    }

    private void startRoute() {
        iMap.startRoute(this, start_point_location, end_point_location, route_type);
    }

    private void setData() {
        endAddress.setText(merchant.address);
    }

    private void initEvent() {
        route_group.setOnCheckedChangeListener(this);
        routeViewPager.addOnPageChangeListener(this);
    }

    private void initComponent() {
        //商户地址
        endAddress = (TextView) findViewById(R.id.tv_end_address);
        //三种类型的路线规划
        routeViewPager = (ViewPager) findViewById(R.id.vp_route);
        route_group = (RadioGroup) findViewById(R.id.rg_route_type_group);


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


}
