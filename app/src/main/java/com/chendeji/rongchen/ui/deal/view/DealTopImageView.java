package com.chendeji.rongchen.ui.deal.view;

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
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.ui.ShowImageActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by chendeji on 28/5/15.
 */
public class DealTopImageView extends RelativeLayout implements View.OnClickListener {

    private final Context mContext;
    private ImageView deal_image;
    private TextView deal_title;
    private TextView deal_description;
    private Deal mDeal;

    public DealTopImageView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public DealTopImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {

        View viewContainer = LayoutInflater.from(mContext).inflate(R.layout.deal_top_image, this, true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.height = MyApplication.SCREEN_HEIGTH / 4;
        params.width = MyApplication.SCREEN_WIDTH ;

        this.setLayoutParams(params);

        initComponent(viewContainer);

    }

    private void initComponent(View viewContainer) {
        deal_image = (ImageView) findViewById(R.id.iv_deal_avatar);
        deal_title = (TextView) findViewById(R.id.tv_deal_title);
        deal_description = (TextView) findViewById(R.id.tv_deal_decription);
        deal_image.setOnClickListener(this);
    }

    public void setComponentValue(Deal deal){
        this.mDeal = deal;
        if (deal == null)
            return ;
        String image_url = deal.image_url;
        deal_title.setText(deal.title);
        deal_description.setText(deal.description);

        ImageLoader.getInstance().displayImage(image_url, deal_image, ImageLoaderOptionsUtil.topImageOptions, MyApplication.imageLoadingListener);
    }


    @Override
    public void onClick(View v) {
        //TODO 跳到图片展示页面
        Intent intent = new Intent(getContext(), ShowImageActivity.class);
        ArrayList<String> photo_urls = new ArrayList<String>();
        photo_urls.addAll(mDeal.more_image_urls);
        intent.putStringArrayListExtra(ShowImageActivity.PHOTOS_KEY, photo_urls);
        getContext().startActivity(intent);
    }
}
