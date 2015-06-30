package com.chendeji.rongchen.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chendeji.rongchen.MyApplication;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ImageLoaderOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by chendeji on 30/6/15.
 */
public class MaterialTopImageView extends FrameLayout {

    private ImageView top_image;
    private View overlayView;

    public MaterialTopImageView(Context context) {
        this(context, null);
    }

    public MaterialTopImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    private void initComponent() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.top_image_layout, this, true);
        top_image = (ImageView) view.findViewById(R.id.iv_top_image);
        overlayView = view.findViewById(R.id.overlay);
    }

    public void setComponnentValue(String photo_url) {

        ImageLoader.getInstance().displayImage(photo_url, top_image,
                ImageLoaderOptionsUtil.topImageOptions, MyApplication.imageLoadingListener);

    }

    //还需要一些方法来控制这里面的颜色透明


}
