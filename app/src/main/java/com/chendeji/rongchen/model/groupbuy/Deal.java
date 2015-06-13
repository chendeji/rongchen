package com.chendeji.rongchen.model.groupbuy;

import java.util.List;

/**
 * 团购的具体详细信息
 * Created by chendeji on 27/5/15.
 */
public class Deal {

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

    public List<SimpleMerchantInfo> businesses;

    public List<String> more_image_urls;

    public Restriction restrictions;
}
