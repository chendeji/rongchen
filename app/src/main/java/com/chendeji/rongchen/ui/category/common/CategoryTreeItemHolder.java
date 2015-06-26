package com.chendeji.rongchen.ui.category.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.chendeji.rongchen.R;
import com.chendeji.rongchen.common.view.treeview.model.TreeNode;

/**
 * Created by chendeji on 26/6/15.
 */
public class CategoryTreeItemHolder extends TreeNode.BaseNodeViewHolder<CategoryTreeItemHolder.CategoryTreeItem> {

    private LayoutInflater inflater;
    private ImageView arrow;
    private ImageView icon;
    private TextView node_value;

    public CategoryTreeItemHolder(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View createNodeView(TreeNode node, CategoryTreeItem value) {
        //1，生成View
        View view = inflater.inflate(R.layout.layout_icon_node, null, false);
        arrow = (ImageView) view.findViewById(R.id.arrow_icon);
        icon = (ImageView) view.findViewById(R.id.icon);
        node_value = (TextView) view.findViewById(R.id.node_value);

        arrow.setBackgroundResource(R.drawable.ic_chevron_right_black_18dp);
        if (node.isLeaf())
            arrow.setVisibility(View.INVISIBLE);

        if(value.iconId != 0){
            icon.setBackground(context.getResources().getDrawable(value.iconId));
        } else {
            icon.setVisibility(View.GONE);
        }
        if (node.getLevel() < 2){
            node_value.setTextSize(14);
        }else {
            node_value.setTextSize(10);
        }
        node_value.setText(value.title);
        return view;
    }

    @Override
    public void toggle(boolean active) {
        if (active) {
            arrow.animate().rotation(90).setInterpolator(new LinearInterpolator()).start();
        } else {
            arrow.animate().rotation(0).setInterpolator(new LinearInterpolator()).start();
        }
    }

    public static class CategoryTreeItem{
        public int iconId;
        public String title;

        public CategoryTreeItem(int iconId, String title) {
            this.iconId = iconId;
            this.title = title;
        }
    }
}
