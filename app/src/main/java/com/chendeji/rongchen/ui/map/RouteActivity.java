package com.chendeji.rongchen.ui.map;

import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chendeji.rongchen.R;
import com.rey.material.widget.TextView;

public class RouteActivity extends AppCompatActivity {

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
    private PagerTitleStrip titleStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_line_planning);
        routeToolBar = (Toolbar) findViewById(R.id.tb_route);
        routeToolBar.setTitle(getString(R.string.check_route));

        initComponent();
        initEvent();

    }

    private void initEvent() {

    }

    private void initComponent() {
        //商户地址
        endAddress = (TextView) findViewById(R.id.tv_end_address);
        //三种类型的路线规划
        routeViewPager = (ViewPager) findViewById(R.id.vp_route);
        titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
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
}
