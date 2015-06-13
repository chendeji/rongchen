package com.chendeji.rongchen.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.chendeji.rongchen.R;

/**
 * Created by chendeji on 31/5/15.
 */
public class CommonRatingBar extends ImageView {

    public CommonRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonRatingBar(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setBackgroundResource(R.drawable.star0);

    }

    public void setRating(float rating){
        if (rating <= 0){
            this.setBackgroundResource(R.drawable.star0);
        } else if (rating > 0 && rating <= 0.5f){
            this.setBackgroundResource(R.drawable.star5);
        } else if (rating > 0.5f && rating <= 1.0f){
            this.setBackgroundResource(R.drawable.star10);
        } else if (rating > 1.0f && rating <= 1.5f){
            this.setBackgroundResource(R.drawable.star15);
        } else if (rating > 1.5f && rating <= 2.0f){
            this.setBackgroundResource(R.drawable.star20);
        } else if (rating > 2.0f && rating <= 2.5f){
            this.setBackgroundResource(R.drawable.star25);
        } else if (rating > 2.5f && rating <= 3.0f){
            this.setBackgroundResource(R.drawable.star30);
        } else if (rating > 3.0f && rating <= 3.5f){
            this.setBackgroundResource(R.drawable.star35);
        } else if (rating > 3.5f && rating <= 4.0f){
            this.setBackgroundResource(R.drawable.star40);
        } else if (rating > 4.0f && rating <= 4.5f){
            this.setBackgroundResource(R.drawable.star45);
        } else if (rating > 4.5f && rating <= 5.0f){
            this.setBackgroundResource(R.drawable.star50);
        }

    }
}
