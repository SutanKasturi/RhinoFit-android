package com.travis.rhinofit.rowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.models.Wall;
import com.travis.rhinofit.models.Wall;
import com.travis.rhinofit.utils.image.SmartImage;
import com.travis.rhinofit.utils.image.SmartImageTask;
import com.travis.rhinofit.utils.image.SmartImageView;
import com.travis.rhinofit.utils.image.WebImageCache;

import java.text.SimpleDateFormat;

/**
 * Created by Sutan Kasturi on 2/12/15.
 */
public class WallRow extends LinearLayout {

    Context context;
    Wall wall;

    TextView msgTextView;
    SmartImageView postImageView;
    SmartImageView userImageView;
    TextView userNameTextView;

    public WallRow(Context context, Wall wall) {
        super(context);
        this.context = context;
        this.wall = wall;
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(final Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(wall.isYours() ? R.layout.row_wall_right : R.layout.row_wall_left, this);

        msgTextView = (TextView) view.findViewById(R.id.msgTextView);
        postImageView = (SmartImageView) view.findViewById(R.id.postImageView);
        userImageView = (SmartImageView) view.findViewById(R.id.userImageView);
        userNameTextView = (TextView) view.findViewById(R.id.userNameTextView);

        setMyWOD();
    }

    private void setMyWOD() {
        if ( wall != null ) {
            msgTextView.setText(wall.getMsg());
            userNameTextView.setText(wall.getName());
            WebImageCache imageCache = new WebImageCache(context);

            if ( wall.getImage() != null && !wall.getImage().isEmpty() ) {
                postImageView.setVisibility(View.VISIBLE);
                Bitmap postImage = imageCache.get(wall.getImage());
                if (postImage != null) {
                    postImageView.setImageBitmap(postImage);
                } else {
                    postImageView.setImageUrl(wall.getImage(), new SmartImageTask.OnCompleteListener() {
                        @Override
                        public void onComplete(Bitmap bitmap) {
                            if ( bitmap != null )
                                postImageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
            }
            else {
                postImageView.setVisibility(View.GONE);
            }

            Bitmap userImage = imageCache.get(wall.getProfileImage());
            if ( userImage != null ) {
                userImageView.setImageBitmap(userImage);
            }
            else {
                userImageView.setImageUrl(wall.getProfileImage(), new SmartImageTask.OnCompleteListener() {
                    @Override
                    public void onComplete(Bitmap bitmap) {
                        userImageView.setImageBitmap(bitmap);
                    }
                    @Override
                    public void onComplete() {

                    }
                });
            }
        }
    }
}
