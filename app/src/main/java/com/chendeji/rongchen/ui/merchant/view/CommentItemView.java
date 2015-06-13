package com.chendeji.rongchen.ui.merchant.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.ColorUtil;
import com.chendeji.rongchen.common.view.SpanableTextView;
import com.chendeji.rongchen.model.comment.Comment;

/**
 * Created by chendeji on 23/5/15.
 */
public class CommentItemView extends LinearLayout {

    private Context mContext;

    private TextView user_name;
    private TextView comment_time;
    private TextView comment_content;

    private SpanableTextView productRating;
    private SpanableTextView decorationRating;
    private SpanableTextView serviceRating;

    public CommentItemView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {

        View itemView = View.inflate(mContext, R.layout.comment_item, this);

        user_name = (TextView) findViewById(R.id.tv_comment_creator);
        comment_time = (TextView) findViewById(R.id.tv_comment_time);
        comment_content = (TextView) findViewById(R.id.tv_comment_text);

        productRating = (SpanableTextView) findViewById(R.id.st_product_rating); //产品评价
        decorationRating = (SpanableTextView) findViewById(R.id.st_decoration_rating); //环境评价
        serviceRating = (SpanableTextView) findViewById(R.id.st_service_rating); //服务评价

        productRating.setGravity(Gravity.CENTER);
        decorationRating.setGravity(Gravity.CENTER);
        serviceRating.setGravity(Gravity.CENTER);

        LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
        this.setOrientation(LinearLayout.VERTICAL);

    }

    public void setComponentValue(Comment comment){
        user_name.setText(comment.user_nickname);
        comment_time.setText(comment.created_time);
        comment_content.setText(comment.text_excerpt);

        productRating.setBackgroundColor(ColorUtil.getBGColorFromRating(comment.product_rating));
        productRating.addPiece(new SpanableTextView.Piece.Builder("  " + comment.product_rating + "  ")
                .backgroundColor(ColorUtil.getColorFromRating(comment.product_rating))
                .textColor(Color.WHITE)
                .build());
//        productRating.addPiece(new SpanableTextView.Piece.Builder(" " +
//                RatingUtil.getCommentTextFromRating(mContext, comment.product_rating) + " ")
//                .textColor(ColorUtil.getColorFromRating(comment.product_rating))
//                .style(Typeface.BOLD)
//                .build());
        productRating.display();

        decorationRating.setBackgroundColor(ColorUtil.getBGColorFromRating(comment.decoration_rating));
        decorationRating.addPiece(new SpanableTextView.Piece.Builder("  " + comment.decoration_rating + "  ")
                .backgroundColor(ColorUtil.getColorFromRating(comment.decoration_rating))
                .textColor(Color.WHITE)
                .build());
//        decorationRating.addPiece(new SpanableTextView.Piece.Builder(" " +
//                RatingUtil.getCommentTextFromRating(mContext, comment.decoration_rating) + " ")
//                .textColor(ColorUtil.getColorFromRating(comment.decoration_rating))
//                .style(Typeface.BOLD)
//                .build());
        decorationRating.display();

        serviceRating.setBackgroundColor(ColorUtil.getBGColorFromRating(comment.service_rating));
        serviceRating.addPiece(new SpanableTextView.Piece.Builder("  " + comment.service_rating + "  ")
                .backgroundColor(ColorUtil.getColorFromRating(comment.service_rating))
                .textColor(Color.WHITE)
                .build());
//        serviceRating.addPiece(new SpanableTextView.Piece.Builder(" " +
//                RatingUtil.getCommentTextFromRating(mContext, comment.service_rating) + " ")
//                .textColor(ColorUtil.getColorFromRating(comment.service_rating))
//                .style(Typeface.BOLD)
//                .build());
        serviceRating.display();
    };


}
