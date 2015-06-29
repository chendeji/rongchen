package com.chendeji.rongchen.model.groupbuy;

import java.io.Serializable;

/**
 * Created by chendeji on 27/5/15.
 */
public class SimpleMerchantInfo implements Serializable{

    public long id;

    public String name;

    public String city;

    public String address;

    public double latitude;

    public double longitude;

    public String h5_url;

    @Override
    public String toString() {
        return "SimpleMerchantInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", h5_url='" + h5_url + '\'' +
                '}';
    }
}
