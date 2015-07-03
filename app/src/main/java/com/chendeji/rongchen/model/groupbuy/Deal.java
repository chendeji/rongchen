package com.chendeji.rongchen.model.groupbuy;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.util.List;

/**
 * 团购的具体详细信息
 * Created by chendeji on 27/5/15.
 */
@Table(name = "Deal")
public class Deal extends SugarRecord {

    @Column(name = "deal_id", unique = true, notNull = true)
    public String deal_id;

    public String title;

    public String description;

    public int list_price;

    public int current_price;

    public int purchase_count;

    public String purchase_deadline;

    public String publish_date;

    public String details;

    public String image_url;

    public String businesses;

    public String more_image_urls;

    public String deal_h5_url;

    public String restrictions;

    public Restriction getRestrictions() {
        if (TextUtils.isEmpty(restrictions))
            return null;
        return JSON.parseObject(restrictions, Restriction.class);
    }

    public List<String> getMore_image_urls() {
        if (TextUtils.isEmpty(more_image_urls))
            return null;
        return JSON.parseArray(more_image_urls, String.class);
    }

    public List<SimpleMerchantInfo> getBusinesses() {
        if (TextUtils.isEmpty(businesses))
            return null;
        return JSON.parseArray(businesses, SimpleMerchantInfo.class);
    }

    @Override
    public String toString() {
        return "Deal{" +
                "deal_id='" + deal_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", list_price=" + list_price +
                ", current_price=" + current_price +
                ", purchase_count=" + purchase_count +
                ", purchase_deadline='" + purchase_deadline + '\'' +
                ", publish_date='" + publish_date + '\'' +
                ", details='" + details + '\'' +
                ", image_url='" + image_url + '\'' +
                ", businesses='" + businesses + '\'' +
                ", more_image_urls='" + more_image_urls + '\'' +
                ", deal_h5_url='" + deal_h5_url + '\'' +
                ", restrictions='" + restrictions + '\'' +
                '}';
    }
}
