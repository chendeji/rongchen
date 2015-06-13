package com.chendeji.rongchen.model.merchant;

import com.chendeji.rongchen.model.Regions;

import java.io.Serializable;
import java.util.List;

import com.chendeji.rongchen.model.Categories;

/**
 * @author chendeji
 * @Description: 商户的描述类
 * @date 27/9/14 下午3:27
 * <p/>
 * ${tags}
 */
public class Merchant implements Serializable{

    /** 商户id*/
    public long business_id;

    /** 商户名称*/
    public String name;

    /** 分店名称*/
    public String branch_name;

    /** 地址*/
    public String address;

    /** 商户电话*/
    public String telephone;

    /** 城市*/
    public String city;

    /** 城市片区*/
    public Regions regions;

    /** 分类*/
    public Categories categories;

    /** 纬度*/
    public double latitude;

    /** 经度*/
    public double longitude;

    /** 人均消费*/
    public int avg_price;

    /** 商户评分*/
    public float avg_rating;

    /** 星级图片链接*/
    public String rating_img_url;

    /** 小尺寸星级图片链接*/
    public String rating_s_img_url;

//    public enum Product_grade {
//        NONE(0),NORMAL(1),STILL(2),GOOD(3),PRITY_GOOD(4),VERY_GOOD(5);
//        private int value;
//        private Product_grade(int value){
//            this.value = value;
//        }
//        public int getValue(){
//            return this.value;
//        }
//    }
    public static final int PRODUCT_GRADE_NONE = 0;
    public static final int PRODUCT_GRADE_NORMAL = 1;
    public static final int PRODUCT_GRADE_STILL = 2;
    public static final int PRODUCT_GRADE_GOOD = 3;
    public static final int PRODUCT_GRADE_PRITY_GOOD = 4;
    public static final int PRODUCT_GRADE_VARY_GOOD = 5;

    /** 食品评价*/
    public int product_grade;

//    public enum Decoration_grade {
//        NONE(0),NORMAL(1),STILL(2),GOOD(3),PRITY_GOOD(4),VERY_GOOD(5);
//        private int value;
//        private Decoration_grade(int value){
//            this.value = value;
//        }
//        public int getValue(){
//            return this.value;
//        }
//    }

    public static final int DECORATION_GRADE_NONE = 0;
    public static final int DECORATION_GRADE_NORMAL = 1;
    public static final int DECORATION_GRADE_STILL = 2;
    public static final int DECORATION_GRADE_GOOD = 3;
    public static final int DECORATION_GRADE_PRITY_GOOD = 4;
    public static final int DECORATION_GRADE_VERY_GOOD = 5;
    /** 环境评价*/
    public int decoration_grade;

//    public enum Service_grade {
//        NONE(0),NORMAL(1),STILL(2),GOOD(3),PRITY_GOOD(4),VERY_GOOD(5);
//        private int value;
//        private Service_grade(int value){
//            this.value = value;
//        }
//        public int getValue(){
//            return this.value;
//        }
//    }

    public static final int SERVICE_GRADE_NONE = 0;
    public static final int SERVICE_GRADE_NORMAL = 1;
    public static final int SERVICE_GRADE_STILL = 2;
    public static final int SERVICE_GRADE_GOOD = 3;
    public static final int SERVICE_GRADE_PRITY_GOOD = 4;
    public static final int SERVICE_GRADE_VERY_GOOD = 5;
    /** 服务评价*/
    public int service_grade;

    /** 产品/食品口味评价单项分，精确到小数点后一位（十分制）*/
    public float product_score;

    /** 环境评价单项分，精确到小数点后一位（十分制）*/
    public float decoration_score;

    /** 服务评价单项分，精确到小数点后一位（十分制）*/
    public float service_score;

    public int review_count;
    public String review_list_url;

    public int distance;
    public String business_url;
    public String photo_url;
    public String s_photo_url;

    public int photo_count;
    public String photo_list_url;

//    public enum Has_Coupon{
//        YES(1),NO(0);
//        private int value;
//        private Has_Coupon(int value){
//            this.value = value;
//        }
//        public int getValue(){
//            return this.value;
//        }
//    }
    public static final int HAS_COUPONS = 1;
    public static final int HASNT_COUPONS = 0;

    /** 是否有优惠劵*/
    public int has_coupon;
    /** 优惠id*/
    public long coupon_id;
    /** 优惠介绍*/
    public String coupon_description;
    /** 优惠券页面链接*/
    public String coupon_url;

//    public enum Has_Deal{
//        YES(1),NO(0);
//        private int value;
//        private Has_Deal(int value){
//            this.value = value;
//        }
//        public int getValue(){
//            return this.value;
//        }
//    }

    public static final int HAS_DEALS = 1;
    public static final int HASNT_DEALS = 0;

    /** 是否有团购*/
    public int has_deal;
    /** 团购数量*/
    public int deal_count;
    /** 团购列表*/
    public List<SimpleGroupBuyInfo> deals;

    public static final int ONLINE_RESERVATION_ABLE = 1;
    public static final int ONLINE_RESERVATION_ANTT_ABLE = 0;

    /** 是否有在线预订*/
    public int has_online_reservation;
    /** 在线预订页面链接，目前仅返回HTML5站点链接*/
    public String online_reservation_url;



}
