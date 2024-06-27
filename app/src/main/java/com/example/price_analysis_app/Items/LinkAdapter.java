package com.example.price_analysis_app.Items;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.price_analysis_app.R;

import java.util.ArrayList;
import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkHolder> {
private static List<Link> linkList=new ArrayList<>();
    private Context context;
    static int  index=0;

    public LinkAdapter(Context context,List<Link> linkList) {
        this.context = context;
        this.linkList=linkList;
    }

    @NonNull
    @Override
    public LinkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("LinkAdapter","onCreateViewHolder_"+index++);
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View viewItem = layoutInflater.inflate(R.layout.activity_link_adapter,parent,false);
        return new LinkHolder(viewItem);

    }

    @Override
    public void onBindViewHolder(@NonNull LinkHolder holder, int position) {
        Link link = linkList.get(position);
        System.out.println("object is oK?" +link.toString());
        System.out.println(link.getSiteLink());
        holder.URL.setText("apasa aici pentru link");
        holder.name.setText(link.getName());
        holder.price.setText(String.valueOf(link.getPrice())+" Lei");

    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }


    public static class LinkHolder extends RecyclerView.ViewHolder{
        protected TextView URL;
        protected TextView name;
        protected TextView price;

        public LinkHolder(@NonNull View itemView) {
            super(itemView);
         URL =itemView.findViewById(R.id.URLtv);
         name= itemView.findViewById(R.id.siteNameTv);
         price = itemView.findViewById(R.id.priceTv);
         URL.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });
        }
    }
}