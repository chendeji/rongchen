package com.chendeji.rongchen;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.chendeji.rongchen.dao.tables.city.RecentSearchCityTable;
import com.chendeji.rongchen.model.city.RecentSearchCity;
import com.chendeji.rongchen.server.AppConst;

import java.util.List;

/**
 * Created by chendeji on 25/6/15.
 */
public class SettingFactory {

    private static final String SETTING = "setting";
    private static final String CITY_SETTING = "city";
    private static final String CATEGORY = "category";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String IS_FIRST_TIME_LOGIN = "is_first_time_login";

    private SharedPreferences sharedPreferences;

    private static SettingFactory ourInstance = new SettingFactory();


    public static SettingFactory getInstance() {
        return ourInstance;
    }

    private SettingFactory() {
    }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_APPEND);
    }

    public void setIsFirstTimeLogin(boolean isFirst) {
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putBoolean(IS_FIRST_TIME_LOGIN, isFirst)
                    .commit();
        }
    }

    public boolean getIsFirstTimeLogin(){
        if (sharedPreferences != null){
            return sharedPreferences.getBoolean(IS_FIRST_TIME_LOGIN, true);
        }
        return false;
    }

    public void setCurrentChoosedCategory(String category) {
        if (TextUtils.isEmpty(category))
            return;
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(CATEGORY, category).commit();
        }
    }

    public String getCurrentChoosedCategory() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(CATEGORY, AppConst.RequestParams.DEFUALT_CATEGORY);
        }
        return null;
    }

    public void addRecentSearchCity(String city) {
        List<RecentSearchCity> recentSearchCities = RecentSearchCity.find(RecentSearchCity.class,
                RecentSearchCityTable.CITY_NAME + "=?", new String[]{city});
        if (recentSearchCities == null || recentSearchCities.isEmpty()) {
            RecentSearchCity recentSearchCity = new RecentSearchCity();
            recentSearchCity.city_name = city;
            recentSearchCity.search_count += 1;
            recentSearchCity.save();
        } else {
            RecentSearchCity recentSearchCity = recentSearchCities.get(0);
            recentSearchCity.search_count += 1;
            recentSearchCity.save();
        }
    }

    public List<RecentSearchCity> getRecentSearchCity() {
        List<RecentSearchCity> recentSearchCities = RecentSearchCity.find(RecentSearchCity.class, null, new String[]{},
                null, RecentSearchCityTable.SEARCH_COUNT + " DESC", "5");

        return recentSearchCities;
    }

    public void setCurrentCity(String city) {
        if (TextUtils.isEmpty(city))
            return;
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(CITY_SETTING, city).commit();
        }
    }

    public String getCurrentCity() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(CITY_SETTING, "");
        }
        return null;
    }

    public void setCurrentLocation(double latitude, double longitude) {
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putString(LATITUDE, String.valueOf(latitude))
                    .putString(LONGITUDE, String.valueOf(longitude)).commit();
        }
    }

    public double[] getCurrentLocation() {
        if (sharedPreferences != null) {
            String str_latitude = sharedPreferences.getString(LATITUDE, "");
            String str_longitude = sharedPreferences.getString(LONGITUDE, "");
            if (TextUtils.isEmpty(str_latitude) || TextUtils.isEmpty(str_longitude)) {
                return null;
            }
            double latitude = Double.parseDouble(str_latitude);
            double longitude = Double.parseDouble(str_longitude);
            return new double[]{latitude, longitude};
        }
        return null;
    }
}
