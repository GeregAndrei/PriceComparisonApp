package com.example.price_analysis_app.Items;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.price_analysis_app.Links.Link;
import com.example.price_analysis_app.Menu.ComparisonManager;
import com.example.price_analysis_app.R;
import com.example.price_analysis_app.Menu.HomeActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        Link link =item.linkList.stream().min(Comparator.comparingDouble(Link::getPrice)).orElse(null);

        Glide.with(holder.itemView.getContext()).load(item.getImgUrl()).into(holder.imgUrl);
        if (link != null) {
            holder.priceAndSite.setText(link.getPrice() + "lei on " + link.getSiteName());

        } else {
            holder.priceAndSite.setText("No link available");
        }
        int pos = itemList.indexOf(item);
        holder.position=pos;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityCallback.onItemClicked(pos);
            }
        });
        holder.compareCheckbox.setChecked(
                ComparisonManager.getInstance().getSelectedItems().contains(item)
        );
        holder.compareCheckbox.setOnCheckedChangeListener((cb, checked) -> {
            if (checked) {
                ComparisonManager.getInstance().addItem(item);
            } else {
                ComparisonManager.getInstance().removeItem(item);
            }
            // Notify hosting Activity to update compare btn
            if (context instanceof HomeActivity) {
                ((HomeActivity) context).updateCompareButtonVisibility();
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
        private TextView priceAndSite;
        private CheckBox compareCheckbox;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemNameTv);
            imgUrl = itemView.findViewById(R.id.itemImageTv);
            priceAndSite=itemView.findViewById(R.id.itemPriceAndSitetv);
            compareCheckbox = itemView.findViewById(R.id.compareCheckBox);
        }

    }
}