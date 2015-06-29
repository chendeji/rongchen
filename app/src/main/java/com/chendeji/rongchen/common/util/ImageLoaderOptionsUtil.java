package com.chendeji.rongchen.common.util;

import android.graphics.Bitmap;

import com.chendeji.rongchen.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by chendeji on 23/5/15.
 */
public class ImageLoaderOptionsUtil {

    //图片显示配置
    public static DisplayImageOptions listItemOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.avatar_96)
            .showImageOnFail(R.drawable.avatar_96)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();


    public static DisplayImageOptions topImageOptions = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    public static DisplayImageOptions ratingbarImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.star0)
            .showImageOnLoading(R.drawable.star0)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();


}
