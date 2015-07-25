package com.chendeji.rongchen.ui.deal.adapter;

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
import com.chendeji.rongchen.model.groupbuy.Deal;
import com.chendeji.rongchen.ui.deal.DealDetailActivity;
import com.chendeji.rongchen.ui.merchant.MerchantDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by chendeji on 9/7/15.
 */
public class DealHistoryAdapter extends RecyclerView.Adapter<DealHistoryAdapter.DealHistoryHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Deal> mDeals;

    public DealHistoryAdapter(Context context, List<Deal> deals) {
        this.mContext = context;
        this.mDeals = deals;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public DealHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.deal_history_item_layout, parent, false);
        return new DealHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(DealHistoryHolder holder, int position) {
        final Deal deal = mDeals.get(position);
        ImageLoader.getInstance().displayImage(deal.image_url, holder.dealImageView,
                ImageLoaderOptionsUtil.topImageOptions, MyApplication.imageLoadingListener);

        holder.dealTitle.setText(deal.description);
        holder.dealCurrentPrice.reset();
        holder.dealCurrentPrice.addPiece(new SpanableTextView.Piece.Builder(String.valueOf(deal.current_price))
                .textColor(mContext.getResources().getColor(R.color.dark_red))
                .textSizeRelative(1.5f)
                .style(Typeface.BOLD)
                .build());
        holder.dealCurrentPrice.addPiece(new SpanableTextView.Piece.Builder("元")
                .textColor(mContext.getResources().getColor(R.color.gray_text))
                .textSizeRelative(0.9f)
                .build());
        holder.dealCurrentPrice.addPiece(new SpanableTextView.Piece.Builder(String.valueOf(deal.list_price) + "元")
                .textColor(mContext.getResources().getColor(R.color.gray_text))
                .textSizeRelative(0.9f)
                .strike()
                .build());
        holder.dealCurrentPrice.display();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入到商户的详细页面
                Intent intent = new Intent(mContext, DealDetailActivity.class);
                intent.putExtra(DealDetailActivity.DEAL_KEY, deal.deal_id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    public void addData(List<Deal> deals) {
        mDeals.addAll(deals);
        notifyDataSetChanged();
    }

    public void refreshData(List<Deal> deals) {
        mDeals.clear();
        mDeals.addAll(deals);
        notifyDataSetChanged();
    }

    class DealHistoryHolder extends RecyclerView.ViewHolder {

        public TextView dealTitle;
        public SpanableTextView dealCurrentPrice;
        public ImageView dealImageView;

        public DealHistoryHolder(View itemView) {
            super(itemView);
            dealImageView = (ImageView) itemView.findViewById(R.id.iv_deal_image);
            dealTitle = (TextView) itemView.findViewById(R.id.tv_history_deal_title);
            dealCurrentPrice = (SpanableTextView) itemView.findViewById(R.id.st_history_and_current_deal_price);
        }
    }
}
