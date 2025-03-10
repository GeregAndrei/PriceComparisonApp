package com.example.price_analysis_app.Items;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.price_analysis_app.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private Context context;
    private static List<Comment> commentList;

    public CommentAdapter(Context context,List<Comment>list){
        this.context=context;
        this.commentList=list;
    }
    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_comment_adapter, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment c =commentList.get(position);
        holder.accountName.setText(c.getAccountName());
        holder.rating.setRating(c.getBar());
        holder.description.setText(c.getDescription());
        int pos =commentList.indexOf(position);
        holder.position=position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentHolder extends RecyclerView.ViewHolder{
        int position;
        private TextView accountName;
        private RatingBar rating;
        private TextView description;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            accountName=itemView.findViewById(R.id.accountNameTv);
            rating=itemView.findViewById(R.id.ratingBarTv);
            description=itemView.findViewById(R.id.descriptionTv);

        }
    }
}
//W7X92OWH