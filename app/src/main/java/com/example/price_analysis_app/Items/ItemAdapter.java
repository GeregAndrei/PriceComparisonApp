package com.example.price_analysis_app.Items;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.price_analysis_app.R;
import com.example.price_analysis_app.uiStuff.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import coil.target.ImageViewTarget;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private Context context;
    private static List<Item> itemList = new ArrayList<>();
    private static List<Item> filteredItems;
    Icallable mainActivityCallback;

    static int index = 0;
    public ItemAdapter(HomeActivity context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.filteredItems = new ArrayList<>(itemList);
        this.mainActivityCallback=context;

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ItemAdapter", "onCreateViewHolder_" + index++);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewItem = layoutInflater.inflate(R.layout.activity_item_adapter, parent, false);
        return new ItemAdapter.ItemHolder(viewItem);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = itemList.get(position);
        holder.name.setText(item.name);

        Glide.with(holder.itemView.getContext()).load(item.getImgUrl()).into(holder.imgUrl);
        int pos = itemList.indexOf(item);
        holder.position=pos;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityCallback.onItemClicked(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        int position;
        private TextView name;
        private ImageView imgUrl;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemNameTv);
            imgUrl = itemView.findViewById(R.id.itemImageTv);


        }

    }
}