package com.chendeji.rongchen.ui.merchant.view;

import android.content.Context;

import com.chendeji.rongchen.model.ReturnMes;
import com.chendeji.rongchen.model.comment.Comment;
import com.chendeji.rongchen.model.merchant.Merchant;
import com.chendeji.rongchen.ui.common.ExtendableHolder;

import java.util.List;

/**
 * Created by chendeji on 25/5/15.
 */
public class CommentExtendableHolder extends ExtendableHolder {

    private final ReturnMes<List<Comment>> comments;

    public CommentExtendableHolder(Context context, ReturnMes<List<Comment>> comments) {
        super(context);
        this.comments = comments;
        listener = new OnShowMoreListener() {
            @Override
            public void showMore() {
                //TODO 点击显示更多之后跳转到WebView显示更加详细的评论信息

            }
        };
        fillData(comments);
    }

    private void fillData(ReturnMes<List<Comment>> comments) {
        if (comments == null)
            return;
        List<Comment> commentList = comments.object;
        if (commentList.size() <= 0)
            return;
        CommentItemView itemView = null;
        for(Comment comment : commentList){
            itemView = new CommentItemView(mContext);
            itemView.setComponentValue(comment);
            getItemHolder().addView(itemView);
        }
    }


}
