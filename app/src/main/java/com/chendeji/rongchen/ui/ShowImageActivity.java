package com.chendeji.rongchen.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ImageLoaderOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ShowImageActivity extends AppCompatActivity {
    public static final String PHOTOS_KEY = "photoes";
    private static List<String> photo_urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        photo_urls = getIntent().getStringArrayListExtra(PHOTOS_KEY);

        Toolbar show_image_toolbar = (Toolbar) findViewById(R.id.tb_show_image);
        show_image_toolbar.setTitle("更多图片");
        show_image_toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(show_image_toolbar);

        ViewPager photo_viewPager = (ViewPager) findViewById(R.id.vp_image_viewPager);
        photo_viewPager.setAdapter(new PhoteViewPagerAdapter());

    }

    private static class PhoteViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return photo_urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView photoView = new ImageView(container.getContext());
            ImageLoader.getInstance().displayImage(photo_urls.get(position), photoView, ImageLoaderOptionsUtil.topImageOptions);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
