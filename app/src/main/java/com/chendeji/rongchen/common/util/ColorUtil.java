package com.chendeji.rongchen.common.util;

import android.graphics.Color;

/**
 * Created by chendeji on 25/5/15.
 */
public class ColorUtil {

    public static final String GOOD = "#2196F3"; //蓝色
    public static final String NORMAL = "#414141"; //黑色
    public static final String HORRIBLE = "#800736"; //红色

    public static final String GOOD_BG = "#DFF1FE"; //浅蓝色背景
    public static final String NORMAL_BG = "#969696"; //灰色背景
    public static final String HORRIBLE_BG = "#FEDFE2"; //浅红背景

    /**
     * 用于根据给定的评定分数来显示颜色
     * @函数名称  :getColorFromRating
     * @brief
     * @see
     * @param rating
     * @return int
     * @author  : chendeji
     * @date  : Mon May 25 20:51:34 CST 2015
     */
    public static int getColorFromRating(int rating) {
        //分三个等级
        if (rating > 3){
            return Color.parseColor(GOOD);
        } else if(rating == 3){
            return Color.parseColor(NORMAL);
        } else {
            return Color.parseColor(HORRIBLE);
        }
    }

    public static int getBGColorFromRating(int rating) {
        //分三个等级
        if (rating > 3){
            return Color.parseColor(GOOD_BG);
        } else if(rating == 3){
            return Color.parseColor(NORMAL_BG);
        } else {
            return Color.parseColor(HORRIBLE_BG);
        }
    }
}
