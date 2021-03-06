package com.edu.upc.businessbook.viewcontrollers.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.upc.businessbook.R;
import com.edu.upc.businessbook.models.Item;
import com.edu.upc.businessbook.viewcontrollers.activities.ClientActivity;
import com.edu.upc.businessbook.viewcontrollers.activities.ItemActivity;

import com.edu.upc.businessbook.viewcontrollers.activities.purchases.PurchaseActivity;
import com.edu.upc.businessbook.viewcontrollers.activities.sales.SaleActivity;

import com.edu.upc.businessbook.viewcontrollers.activities.OnBoardActivity;
import com.edu.upc.businessbook.viewcontrollers.activities.ProductActivity;
import com.edu.upc.businessbook.viewcontrollers.activities.ProviderActivity;


import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private List<Item> items;
    Item item;

    public ItemsAdapter(List<Item> items) {
        this.items = items;
    }

    public ItemsAdapter() {
    }

    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_local, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ViewHolder holder, int position) {
        holder.updateView(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Item> getItems() {
        return items;
    }

    public ItemsAdapter setItems(List<Item> items) {
        this.items = items;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemLocalImageView;
        CardView itemLocalLayout;
        TextView nameTextView;
        public ViewHolder(View view) {
            super(view);
            itemLocalImageView = (ImageView) view.findViewById(R.id.itemLocalImageView);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            itemLocalLayout = (CardView) view.findViewById(R.id.layout_itemLocal);
        }
        private void updateView(final Item item){

            nameTextView.setText(item.getName());
            itemLocalImageView.setImageResource(item.getThumbnail());
            itemLocalImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getName().toString() == "Sales") {
                        Context context = v.getContext();
                        context.startActivity(new Intent(context, SaleActivity.class)
                                .putExtras(item.toBundle()));
                    }
                    if(item.getName().toString() == "Providers") {
                        Context context = v.getContext();
                        context.startActivity(new Intent(context, ProviderActivity.class)
                                .putExtras(item.toBundle()));
                    }
                    if(item.getName().toString() == "Purchases") {
                        Context context = v.getContext();
                        context.startActivity(new Intent(context, PurchaseActivity.class));
                    }

                    if(item.getName().toString() == "Products") {
                        Context context = v.getContext();
                        context.startActivity(new Intent(context, ProductActivity.class)
                                .putExtras(item.toBundle()));
                    }
                    if(item.getName().toString() == "Clients") {
                        Context context = v.getContext();
                        context.startActivity(new Intent(context, ClientActivity.class)

                                .putExtras(item.toBundle()));
                    }
                }
            });
        }
    }
}
