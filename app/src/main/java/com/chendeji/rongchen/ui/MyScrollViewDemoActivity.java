package com.chendeji.rongchen.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.view.AutoHorizontalLinearLayout;
import com.rey.material.widget.TextView;

import java.util.Random;

public class MyScrollViewDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scroll_view_demo);

        AutoHorizontalLinearLayout linearLayout = (AutoHorizontalLinearLayout) findViewById(R.id.ahll_title_layout);
        TextView textView;
        int padding = getResources().getDimensionPixelSize(R.dimen.horizontal_10dp);
        for (int i = 0; i < 100; i++) {
            double number = new Random().nextDouble();
            textView = new TextView(this);
            textView.setTextSize(30);
            textView.setText(String.valueOf(number));
            textView.setPadding(padding, padding, padding, padding);
            textView.setBackgroundColor(getResources().getColor(R.color.common_button_textcolor));
            textView.setTextColor(getResources().getColor(R.color.white));
            linearLayout.addView(textView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_scroll_view_demo, menu);
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
