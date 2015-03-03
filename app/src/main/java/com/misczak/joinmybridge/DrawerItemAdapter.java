package com.misczak.joinmybridge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by misczak on 3/2/15.
 */
public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.DrawerItemViewHolder> {


    private LayoutInflater inflater;
    List<DrawerItem> drawerItems = Collections.emptyList();

    public DrawerItemAdapter (Context context, List<DrawerItem> drawerItems) {
        inflater = LayoutInflater.from(context);
        this.drawerItems = drawerItems;
    }

    @Override
    public DrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.drawer_item_row, parent, false);
        DrawerItemViewHolder holder = new DrawerItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DrawerItemViewHolder holder, int position) {

        DrawerItem currentDrawerItem = drawerItems.get(position);

        holder.drawerItemName.setText(currentDrawerItem.drawerItemTitle);
        holder.drawerItemImage.setImageResource(currentDrawerItem.drawerItemIconId);
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    class DrawerItemViewHolder extends RecyclerView.ViewHolder {

        TextView drawerItemName;
        ImageView drawerItemImage;

        public DrawerItemViewHolder(View itemView) {
            super(itemView);

            drawerItemName = (TextView) itemView.findViewById(R.id.drawerItemName);
            drawerItemImage = (ImageView) itemView.findViewById(R.id.drawerItemId);

        }
    }
}
