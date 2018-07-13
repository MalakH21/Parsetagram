package com.codepath.parseinstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.parseinstagram.model.Post;
import com.parse.ParseException;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    TextView tvUserHandle;
    ImageView ivUserPost;
    ImageView ivProfileImage;
    TextView tvTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUserHandle = (TextView) findViewById(R.id.tvUserHandle);
        ivUserPost = (ImageView) findViewById(R.id.ivUserPost);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvTime = (TextView) findViewById(R.id.tvTime);

        Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
//        tvUserHandle.setText(getIntent().getStringExtra("handle"));
        try {
            tvUserHandle.setText(post.getHandle());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvTime.setText(post.getCreatedAt().toString());

        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(ivUserPost);


    }


}


