package com.travis.rhinofit.rowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.interfaces.WallCellButtonDelegate;
import com.travis.rhinofit.models.Wall;
import com.travis.rhinofit.models.Wall;
import com.travis.rhinofit.utils.image.SmartImage;
import com.travis.rhinofit.utils.image.SmartImageTask;
import com.travis.rhinofit.utils.image.SmartImageView;
import com.travis.rhinofit.utils.image.WebImageCache;

import org.w3c.dom.Text;

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
    ImageView btnFlag;
    TextView btnRemove;

    int index;
    WallCellButtonDelegate buttonDelegate;

    public WallRow(Context context, Wall wall, int index, WallCellButtonDelegate buttonDelegate) {
        super(context);
        this.context = context;
        this.wall = wall;
        this.index = index;
        this.buttonDelegate = buttonDelegate;
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
        btnFlag = (ImageView) view.findViewById(R.id.btnFlag);
        btnRemove = (TextView) view.findViewById(R.id.btnRemove);

        if (btnFlag != null)
        btnFlag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDelegate.onFlagClicked(index);
            }
        });

        if (btnRemove != null)
        btnRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDelegate.onDelteClicked(index);
            }
        });

        setMyWOD();
    }

    private void setMyWOD() {
        if ( wall != null ) {
            msgTextView.setText(wall.getMsg());
            userNameTextView.setText(wall.getName());

            if (wall.getFlaggable())
                if (wall.isYours())
                    btnRemove.setVisibility(VISIBLE);
                else
                    btnFlag.setVisibility(VISIBLE);
            else
                if (wall.isYours())
                    btnRemove.setVisibility(INVISIBLE);
                else
                    btnFlag.setVisibility(INVISIBLE);

//            WebImageCache imageCache = new WebImageCache(context);

            if ( wall.getImage() != null && !wall.getImage().isEmpty() ) {
                System.out.println(wall.getMsg() + ":" + wall.getImage());
                postImageView.setVisibility(View.VISIBLE);
//                Bitmap postImage = imageCache.get(wall.getImage());
//                if (postImage != null) {
//                    postImageView.setImageBitmap(postImage);
//                } else {
                    postImageView.setImageUrl(wall.getImage(), new SmartImageTask.OnCompleteListener() {
                        @Override
                        public void onComplete(Bitmap bitmap) {
                            if ( bitmap != null )
                                postImageView.setImageBitmap(bitmap);
                            else
                                wall.setImage("");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
//                }
            }
            else {
                postImageView.setVisibility(View.GONE);
            }

//            Bitmap userImage = imageCache.get(wall.getProfileImage());
//            if ( userImage != null ) {
//                userImageView.setImageBitmap(userImage);
//            }
//            else {
                userImageView.setImageUrl(wall.getProfileImage(), new SmartImageTask.OnCompleteListener() {
                    @Override
                    public void onComplete(Bitmap bitmap) {
                        userImageView.setImageBitmap(bitmap);
                    }
                    @Override
                    public void onComplete() {

                    }
                });
//            }
        }
    }
}
