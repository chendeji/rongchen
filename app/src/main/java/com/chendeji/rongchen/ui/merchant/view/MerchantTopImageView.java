package com.chendeji.rongchen.ui.merchant.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chendeji.rongchen.MyApplication;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ImageLoaderOptionsUtil;
import com.chendeji.rongchen.common.view.CommonRatingBar;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.ShowImageActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 * 详情顶部的图片显示布局
 * @ClassName:CommonTopImageView
 * @Function:
 * @Reason:
 * @author $author
 * @date Mon Jan 19 21:14:00 CST 2015
 * @see
 */
public class MerchantTopImageView extends RelativeLayout implements View.OnClickListener {

    private ImageView imageView;
    private TextView textView;
    private CommonRatingBar iv_ratingBar;
    private Merchant mMerchant;


    public MerchantTopImageView(Context context) {
        super(context);
        init(context,null);
    }

    public MerchantTopImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.merchant_top_image, this, true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.height = MyApplication.SCREEN_HEIGTH / 4;
        params.width = MyApplication.SCREEN_WIDTH ;

        this.setLayoutParams(params);

        //加载子控件
        initComponent(container);
    }

    public void setComponentValue(Merchant merchant){
        this.mMerchant = merchant;
        textView.setText(merchant.name);
        iv_ratingBar.setRating(merchant.avg_rating);
        //TODO 加载图片
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(merchant.photo_url, imageView, ImageLoaderOptionsUtil.topImageOptions, MyApplication.imageLoadingListener);
//        loader.displayImage(merchant.rating_s_img_url, iv_ratingBar, ImageLoaderOptionsUtil.ratingbarImageOptions);
    }

    private void initComponent(View container) {
        imageView = (ImageView) container.findViewById(R.id.iv_merchant_avatar);
        textView = (TextView) container.findViewById(R.id.tv_top_merchant_name);
        iv_ratingBar = (CommonRatingBar) container.findViewById(R.id.iv_dingping_rating_image);
        //初始化事件
        imageView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        //TODO 点击获取跳转到更多图片webView
        ArrayList<String> photo_urls = new ArrayList<String>();
        photo_urls.add(mMerchant.photo_url);
        Intent intent = new Intent(getContext(), ShowImageActivity.class);
        intent.putStringArrayListExtra(ShowImageActivity.PHOTOS_KEY, photo_urls);
        getContext().startActivity(intent);
    }
}
