package com.chendeji.rongchen.common.util;

import android.content.Context;

import com.chendeji.rongchen.R;

/**
 * Created by chendeji on 25/5/15.
 */
public class RatingUtil {


    public static String getCommentTextFromRating(Context context, int product_rating) {
        if (product_rating > 3){
            return context.getString(R.string.GOOD);
        } else if(product_rating == 3){
            return context.getString(R.string.NORMAL);
        } else {
            return context.getString(R.string.HORRIBLE);
        }
    }
}
