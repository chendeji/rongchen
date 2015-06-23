package com.chendeji.rongchen.model.merchant;


import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * @author chendeji
 * @Description: 在返回的商户数据中带有的简单的团购信息
 * @date 27/9/14 下午6:29
 * <p/>
 * ${tags}
 */
@Table(name = "SimpleGroupBuyInfo")
public class SimpleGroupBuyInfo extends SugarRecord implements Serializable{

    @Column(name = "groupbuy_id", unique = true, notNull = true)
    public String id;

    public String description;

    public String url;

}
