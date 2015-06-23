package com.chendeji.rongchen.ui.city.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 23/6/15.
 */
public class CityAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<String> mCities;
    private LayoutInflater inflater;

    public CityAdapter(Context context){
        this.mContext = context;
        this.mCities = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);

    }

    public void setData(List<String> cities){
        if (mCities.size() > 0)
            mCities.clear();
        mCities.addAll(cities);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCities.size();
    }

    @Override
    public Object getItem(int position) {
        return mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mCities.get(position));

        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
