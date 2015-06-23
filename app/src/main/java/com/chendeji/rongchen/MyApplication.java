package com.chendeji.rongchen;

import com.chendeji.rongchen.common.map.MapManager;
import com.chendeji.rongchen.dao.DBFactory;
import com.chendeji.rongchen.model.Offset_Type;
import com.chendeji.rongchen.server.AppServerConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {

    public static SimpleImageLoadingListener imageLoadingListener = new SimpleImageLoadingListener(){
        final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    };

    DisplayMetrics metrics = new DisplayMetrics();

    /** 手机的宽度*/
    public static int SCREEN_WIDTH;
    /** 手机的高度*/
    public static int SCREEN_HEIGTH;

    @Override
    public void onCreate() {

        WindowManager wm = (WindowManager)this.getSystemService(this.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        SCREEN_WIDTH = metrics.widthPixels;
        SCREEN_HEIGTH = metrics.heightPixels;

        //初始化图片加载器
        initImageLoader();

        //初始化注入接口
        AppServerConfig.getInstance().initServerFactory();

        //初始化地图
        MapManager manager = MapManager.getManager();
        manager.init(this);
        manager.setOffset_type(Offset_Type.GAODE);  //设定高德地图为坐标偏移

        //初始化数据库
        DBFactory.getInstance().init(this);

        super.onCreate();

    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(5)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        ImageLoader.getInstance().destroy();
        AppServerConfig.destoryFactory();
        DBFactory.getInstance().destroy();
        MapManager.getManager().release();
        super.onTerminate();
    }
}
