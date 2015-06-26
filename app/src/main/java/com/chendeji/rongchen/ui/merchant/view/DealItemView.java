package com.chendeji.rongchen.ui.merchant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ImageLoaderOptionsUtil;
import com.chendeji.rongchen.model.merchant.SimpleGroupBuyInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by chendeji on 1/2/15.
 */
public class DealItemView extends RelativeLayout {

    private ImageView imageView;
    private TextView dealName;


    public DealItemView(Context context) {
        super(context);
        init(context, null);
    }

    public DealItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        View view = LayoutInflater.from(context).inflate(R.layout.deal_item, this, true);
        imageView = (ImageView) view.findViewById(R.id.iv_deal_avator);
        dealName = (TextView) view.findViewById(R.id.tv_deal_name);

        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setBackgroundColor(context.getResources().getColor(R.color.white));

    }

    public void setComponentValue(SimpleGroupBuyInfo info){

        dealName.setText(info.description);
        String photo_url = info.url;
        String groupBugID = info.id;
        ImageLoader.getInstance().displayImage(photo_url, imageView, ImageLoaderOptionsUtil.listItemOptions);
    }

}
