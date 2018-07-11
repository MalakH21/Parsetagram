package com.codepath.parseinstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.parseinstagram.model.Post;

import java.util.List;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder>{

    private List<Post> mPosts;
    Context context;
    public InstaAdapter(List<Post> posts){
        mPosts = posts;
    }

    @Override
    public InstaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_feed, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        // get the data according to position
        Post post = mPosts.get(i);
        // populate the views according to this data
        holder.tvHandle.setText(post.getHandle());
        holder.tvCaption.setText(post.getDescription());

        Glide.with(context).load(post.getImage().getUrl()).into(holder.ivPic);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    //create ViewHolder class
    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvCaption;
        public TextView tvHandle;
        public ImageView ivPic;
        //public Button reply;

        public ViewHolder(View itemView){
            super(itemView);
            // perform findViewById lookups
            ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);


        }
    }

    //clear all elements
    public void clear(){
        mPosts.clear();
        notifyDataSetChanged();
    }
}
