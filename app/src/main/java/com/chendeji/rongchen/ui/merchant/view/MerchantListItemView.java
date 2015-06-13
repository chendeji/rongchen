package com.chendeji.rongchen.ui.merchant.view;

import com.chendeji.rongchen.MyApplication;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ImageLoaderOptionsUtil;
import com.chendeji.rongchen.common.view.CommonRatingBar;
import com.chendeji.rongchen.common.view.SpanableTextView;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author chendeji
 * @Description: 商户列表的item
 * @date 11/11/14 下午10:27
 * <p/>
 * ${tags}
 */
public class MerchantListItemView extends CardView {

    /**
     * 商户头像控件
     */
    private ImageView mMerchantAvatar;
    /**
     * 商户名称
     */
    private TextView mMerchantName;
    /**
     * 商户评分
     */
    private CommonRatingBar mMerchantRating;
    /**
     * 商户的人均消费
     */
    private SpanableTextView mMerchantAvgPrice;

    public MerchantListItemView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * 初始化控件
     *
     * @param context 上下文句柄
     */
    private void initView(Context context) {
//        View mContainView = LayoutInflater.from(context).inflate(R.layout.merchant_list_item_holder, this, true);
        View mContainView = LayoutInflater.from(context).inflate(R.layout.merchant_list_item, this, true);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        setRadius(context.getResources().getDimensionPixelSize(R.dimen.cardview_radius));
//        mMerchantAvatar = (ImageView) mContainView.findViewById(R.id.iv_merchant_avatar);
        mMerchantAvatar = (ImageView) mContainView.findViewById(R.id.iv_merchant_item_image);
//        mMerchantName = (TextView) mContainView.findViewById(R.id.tv_item_merchant_name);
        mMerchantName = (TextView) mContainView.findViewById(R.id.tv_merchant_item_title);

        //人均
        mMerchantAvgPrice = (SpanableTextView) mContainView.findViewById(R.id.st_merhant_item_avg_price);

//        mMerchantRating = (CommonRatingBar) mContainView.findViewById(R.id.iv_merchant_rating);
        mMerchantRating = (CommonRatingBar) mContainView.findViewById(R.id.iv_merchant_item_rating);
//        mCommontCount = (TextView) mContainView.findViewById(R.id.tv_merchant_commont_count);
//        mMerchantAvgPrice = (TextView) mContainView.findViewById(R.id.tv_avg_price);
    }

    /**
     * 填充数据
     *
     * @param merchant 商户信息
     */
    public void setData(Merchant merchant) {
        if (merchant == null) {
            return;
        }
        mMerchantName.setText(merchant.name);
        mMerchantRating.setRating(merchant.avg_rating);
//        mCommontCount.setText(String.format("%d评价", merchant.review_count));
//        mMerchantAvgPrice.setText(String.format("人均%d元", merchant.avg_price));

        mMerchantAvgPrice.reset();
        mMerchantAvgPrice.addPiece(new SpanableTextView.Piece.Builder("人均")
                .textSizeRelative(0.9f)
                .textColor(getContext().getResources().getColor(R.color.gray_text))
                .build());
        mMerchantAvgPrice.addPiece(new SpanableTextView.Piece.Builder(String.valueOf(merchant.avg_price))
                .textColor(getContext().getResources().getColor(R.color.dark_red))
                .textSizeRelative(1.5f)
                .style(Typeface.BOLD)
                .build());
        mMerchantAvgPrice.addPiece(new SpanableTextView.Piece.Builder("元")
                .textSizeRelative(0.9f)
                .textColor(getContext().getResources().getColor(R.color.gray_text))
                .build());
        mMerchantAvgPrice.display();

        //TODO 下载图片的功能
        String s_photo_url = merchant.s_photo_url;
        mMerchantAvatar.setTag(s_photo_url);

        ImageLoader.getInstance().displayImage(s_photo_url, mMerchantAvatar, ImageLoaderOptionsUtil.listItemOptions,
                MyApplication.imageLoadingListener);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //将Ratingbar的事件拦截掉
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
