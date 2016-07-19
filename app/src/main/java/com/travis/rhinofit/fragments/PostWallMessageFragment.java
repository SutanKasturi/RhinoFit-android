package com.travis.rhinofit.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.base.BaseImageChooserFragment;
import com.travis.rhinofit.customs.CustomEditText;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.RealPathUtil;
import com.travis.rhinofit.utils.UtilsMethod;
import com.travis.rhinofit.utils.UtilsValues;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/16/15.
 */
public class PostWallMessageFragment extends BaseImageChooserFragment {
    CustomEditText messageEditText;
    Button pictureButton;
    ImageView postImageView;
    Button postButton;

    Bitmap postPicture = null;

    ProgressDialog progressDialog = null;
    PostWallMessageListener listener;

    public static PostWallMessageFragment newInstance(PostWallMessageListener callback) {
        PostWallMessageFragment postWallMessageFragment = new PostWallMessageFragment();
        postWallMessageFragment.listener = callback;
        return postWallMessageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_wall_frame, container, false);

        messageEditText = (CustomEditText) view.findViewById(R.id.messageEditText);
        pictureButton = (Button) view.findViewById(R.id.pictureButton);
        postImageView = (ImageView) view.findViewById(R.id.postImageView);
        postButton = (Button) view.findViewById(R.id.postWallButton);

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( postPicture == null ) {
                    showDialogSelectPhoto();
                }
                else {
                    AlertUtil.confirmationAlert(parentActivity, "", "Are you sure to delete this picture?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeImage();
                        }
                    });
                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostWall();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setNavTitle();
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle("Post a Wall Message");
    }



    @Override
    public void init() {
        messageEditText.setText("");
        removeImage();
        isCrop = true;
        isSquare = false;
    }

    private void removeImage() {
        pictureButton.setText("Add Picture");
        postImageView.setImageBitmap(null);
        postImageView.setVisibility(View.GONE);
        postPicture = null;
    }

    @Override
    public void setImage(Uri imageUri) {
//        Uri uriFromPath = Uri.fromFile(new File(imageUri.getPath()));
        try {
            postPicture = BitmapFactory.decodeStream(parentActivity.getContentResolver().openInputStream(imageUri));
            postImageView.setImageBitmap(postPicture);
            postImageView.setVisibility(View.VISIBLE);
            pictureButton.setText("Remove Picture");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onPostWall() {
        if ( messageEditText.isValidInput() ) {
            InputMethodManager imm = (InputMethodManager)parentActivity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);

            progressDialog = new ProgressDialog(parentActivity);
            progressDialog.setMessage("Posting...");
            progressDialog.show();

            WebService.addWallPostWithImage(parentActivity, postPicture, filePath, messageEditText.getText().toString(), new InterfaceHttpRequest.HttpRequestJsonListener() {
                @Override
                public void complete(JSONObject result, String errorMsg) {
                    progressDialog.dismiss();
                    if ( result != null ) {
                        if ( listener != null )
                            listener.didPostWallMessageWithImage();
                        if (UtilsValues.messageHandler != null)
                            UtilsValues.messageHandler.sendEmptyMessage(2);
                        parentActivity.onBackPressed();
                    }
                    else {
                        if ( errorMsg == null ) {
                            errorMsg = "Failure add wall post with image";
                        }
                        AlertUtil.messageAlert(parentActivity, "Error", errorMsg);
                    }
                }
            }).onRun();
        }
    }

    public interface PostWallMessageListener {
        public void didPostWallMessageWithImage();
    }
}
