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
    private Context context;
    private ClickListener clickListener;

    List<DrawerItem> drawerItems = Collections.emptyList();

    public DrawerItemAdapter (Context context, List<DrawerItem> drawerItems) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.drawerItems = drawerItems;
    }

    @Override
    public DrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.drawer_item_row, parent, false);
        DrawerItemViewHolder holder = new DrawerItemViewHolder(view);
        return holder;
    }

    //remove final from position
    @Override
    public void onBindViewHolder(DrawerItemViewHolder holder, int position) {

        DrawerItem currentDrawerItem = drawerItems.get(position);

        holder.drawerItemName.setText(currentDrawerItem.drawerItemTitle);
        holder.drawerItemImage.setImageResource(currentDrawerItem.drawerItemIconId);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    class DrawerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView drawerItemName;
        ImageView drawerItemImage;

        public DrawerItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            drawerItemName = (TextView) itemView.findViewById(R.id.drawerItemName);
            drawerItemImage = (ImageView) itemView.findViewById(R.id.drawerItemId);

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }

}
