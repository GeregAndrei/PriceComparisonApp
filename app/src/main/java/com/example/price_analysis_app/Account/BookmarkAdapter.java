package com.example.price_analysis_app.Account;


import com.example.price_analysis_app.Items.Item;

import com.example.price_analysis_app.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.price_analysis_app.Links.Link;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder> {

    private Context context;
    private static List<Item> itemList = new ArrayList<>();
    private static List<Item> filteredItems;
    Icallable2 mainActivityCallback2;

    static int index = 0;
    public BookmarkAdapter(BookmarkActivity context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.filteredItems = new ArrayList<>(itemList);
        this.mainActivityCallback2=context;

    }

    @NonNull
    @Override
    public BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ItemAdapter", "onCreateViewHolder_" + index++);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewItem = layoutInflater.inflate(R.layout.activity_item_adapter, parent, false);
        return new BookmarkAdapter.BookmarkHolder(viewItem);

    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.BookmarkHolder holder, int position) {
        Item item = itemList.get(position);
        holder.name.setText(item.getName());
        Link link = item.getLinkList().stream().min(Comparator.comparingDouble(Link::getPrice)).orElse(null);

        Glide.with(holder.itemView.getContext()).load(item.getImgUrl()).into(holder.imgUrl);
        if (link != null) {
            holder.priceAndSite.setText(link.getPrice() + "lei on " + link.getName());
        } else {
            holder.priceAndSite.setText("No link available");
        }
        int pos = itemList.indexOf(item);
        holder.position=pos;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityCallback2.onItemClicked2(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class BookmarkHolder extends RecyclerView.ViewHolder {
        int position;
        private TextView name;
        private ImageView imgUrl;
        private TextView priceAndSite;


        public BookmarkHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemNameTv);
            imgUrl = itemView.findViewById(R.id.itemImageTv);
            priceAndSite=itemView.findViewById(R.id.itemPriceAndSitetv);
        }

    }
}