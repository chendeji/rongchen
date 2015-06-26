package com.chendeji.rongchen.ui.category.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chendeji.rongchen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendeji on 25/6/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private final LayoutInflater mLayoutInflater;
    private List<String> mCategories;
    private OnItemViewClickedListener mListener;

    public void setData(List<String> categories) {
        if (mCategories != null || mCategories.size() > 0){
            mCategories.clear();
            mCategories.addAll(categories);
        } else {
            mCategories = categories;
        }
        notifyDataSetChanged();
    }

    public interface OnItemViewClickedListener{
        void onClick(String category);
    }

    public MyAdapter(Context context, ArrayList<String> categories, OnItemViewClickedListener listener){
        mLayoutInflater = LayoutInflater.from(context);
        this.mCategories = categories;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_text, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textView.setText(mCategories.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(mCategories.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

}