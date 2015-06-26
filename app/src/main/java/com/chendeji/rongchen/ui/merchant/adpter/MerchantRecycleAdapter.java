package com.chendeji.rongchen.ui.merchant.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chendeji.rongchen.MyApplication;
import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ImageLoaderOptionsUtil;
import com.chendeji.rongchen.common.view.CommonRatingBar;
import com.chendeji.rongchen.common.view.SpanableTextView;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.merchant.MerchantDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 3/6/15.
 */
public class MerchantRecycleAdapter extends RecyclerView.Adapter<MerchantRecycleAdapter.MerchantItemViewHolder> {

    private final Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Merchant> mMerchants;

    public MerchantRecycleAdapter(Context context, List<Merchant> merchants){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mMerchants = merchants;
    }

    @Override
    public MerchantItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewContainer = mLayoutInflater.inflate(R.layout.merchant_cardview_item_layout, parent, false);
        return new MerchantItemViewHolder(viewContainer);
    }

    @Override
    public void onBindViewHolder(MerchantItemViewHolder holder, final int position) {
        final Merchant merchant = mMerchants.get(position);

        ImageLoader.getInstance().displayImage(merchant.photo_url, holder.merchant_image, ImageLoaderOptionsUtil.listItemOptions,
                MyApplication.imageLoadingListener);

        holder.merchant_title.setText(merchant.name);
        holder.merchant_ratting.setRating(merchant.avg_rating);
        holder.avg_price.reset();
        holder.avg_price.addPiece(new SpanableTextView.Piece.Builder("人均")
                .textSizeRelative(0.9f)
                .textColor(mContext.getResources().getColor(R.color.gray_text))
                .build());
        holder.avg_price.addPiece(new SpanableTextView.Piece.Builder(String.valueOf(merchant.avg_price))
                .textColor(mContext.getResources().getColor(R.color.dark_red))
                .textSizeRelative(1.5f)
                .style(Typeface.BOLD)
                .build());
        holder.avg_price.addPiece(new SpanableTextView.Piece.Builder("元")
                .textSizeRelative(0.9f)
                .textColor(mContext.getResources().getColor(R.color.gray_text))
                .build());
        holder.avg_price.display();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入到商户的详细页面
                Intent intent = new Intent(mContext, MerchantDetailActivity.class);
                intent.putExtra(MerchantDetailActivity.MERCHANT_ID, merchant.business_id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMerchants.size();
    }

    public void setData(List<Merchant> returnMes, boolean isAdd) {
        if (returnMes == null) {
            mMerchants = new ArrayList<Merchant>();
            mMerchants.addAll(returnMes);
        } else {
            if (!isAdd){
                if (!mMerchants.isEmpty()) {
                    mMerchants.clear();
                }
            }
            mMerchants.addAll(returnMes);
        }

        // update list
        notifyDataSetChanged();
    }

    public int getCount() {
        return mMerchants.size();
    }

    class MerchantItemViewHolder extends RecyclerView.ViewHolder{

        ImageView merchant_image;
        TextView merchant_title;
        CommonRatingBar merchant_ratting;
        SpanableTextView avg_price;
        public MerchantItemViewHolder(View itemView) {
            super(itemView);
            merchant_image = (ImageView) itemView.findViewById(R.id.iv_merchant_item_image);
            merchant_title = (TextView) itemView.findViewById(R.id.tv_merchant_item_title);
            merchant_ratting = (CommonRatingBar) itemView.findViewById(R.id.iv_merchant_item_rating);
            avg_price = (SpanableTextView) itemView.findViewById(R.id.st_merhant_item_avg_price);
        }
    }
}
