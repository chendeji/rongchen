package com.chendeji.rongchen.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.util.Logger;

/**
 * Created by chendeji on 19/6/15.
 */
public class CommonSearchLayout extends RelativeLayout {

    private String hint;
    private Drawable left_image;
    private Drawable right_cancel_image;
    private ImageView search_image;
    private EditText ed_text;
    private ImageView cancel_image;

    private OnSearchKeyWordChanged keyWordChanged;

    public interface OnSearchKeyWordChanged{
        void onKeyChanged(String keyWord);
        void onKeyClear();
    }

    public void setKeyWordChangedListener(OnSearchKeyWordChanged listener){
        this.keyWordChanged = listener;
    }

    public CommonSearchLayout(Context context) {
        super(context);
        init();
    }

    public CommonSearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeValue(context, attrs, 0);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CommonSearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeValue(context, attrs, defStyleAttr);
        init();
    }

    private void initTypeValue(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonSearchLayout, defStyleAttr, 0);
        hint = typedArray.getString(R.styleable.CommonSearchLayout_edit_text_hint);
        left_image = typedArray.getDrawable(R.styleable.CommonSearchLayout_left_image);
        right_cancel_image = typedArray.getDrawable(R.styleable.CommonSearchLayout_cancel_image);
        typedArray.recycle();
    }

    private void init() {
        View viewContainer = LayoutInflater.from(getContext()).inflate(R.layout.common_search_layout, this, true);
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.horizontal_10dp);
        setPadding(padding, padding, padding, padding);
        search_image = (ImageView) viewContainer.findViewById(R.id.iv_left_search_image);
        ed_text = (EditText) viewContainer.findViewById(R.id.et_city_input);
        cancel_image = (ImageView) viewContainer.findViewById(R.id.iv_right_cancel_image);

        if (left_image != null){
            search_image.setImageDrawable(left_image);
        }else{
            search_image.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.ic_menu_search));
        }

        if (right_cancel_image != null){
            cancel_image.setImageDrawable(right_cancel_image);
        }else{
            cancel_image.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
        }

        initData();
        initEvent();
    }

    private void initData() {
        //主页面初始化时调用
        ed_text.setHint(hint);
        hideCancelButton();
        if (keyWordChanged != null){
            keyWordChanged.onKeyClear();
        }
    }

    private void hideCancelButton(){
        cancel_image.setVisibility(View.INVISIBLE);
    }

    private void showCancelButton(){
        cancel_image.setVisibility(View.VISIBLE);
    }

    private void initEvent() {
        cancel_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_text.setText("");
                if (keyWordChanged != null){
                    keyWordChanged.onKeyClear();
                }
            }
        });
        ed_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_key = s.toString();
                int length = search_key.length();
                if (length > 0) {
                    showCancelButton();
                }
                //回调函数
                if (keyWordChanged != null) {
                    keyWordChanged.onKeyChanged(search_key);
                }

            }
        });
    }
}
