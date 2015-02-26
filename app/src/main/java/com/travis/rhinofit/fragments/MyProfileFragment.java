package com.travis.rhinofit.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.WallArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.customs.WaitingLayout;
import com.travis.rhinofit.global.AppManager;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.User;
import com.travis.rhinofit.models.Wall;
import com.travis.rhinofit.utils.image.SmartImage;
import com.travis.rhinofit.utils.image.SmartImageTask;
import com.travis.rhinofit.utils.image.SmartImageView;
import com.travis.rhinofit.utils.image.WebImageCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class MyProfileFragment extends BaseFragment implements UpdateProfileFragment.UpdateProfileListener {
    SmartImageView avatarImageView;
    TextView nameTextView;
    TextView addressTextView;
    TextView phoneTextView;
    TextView emailTextView;

    Button updateMyProfileButton;

    AppManager appManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_frame, container, false);

        avatarImageView = (SmartImageView) view.findViewById(R.id.avatarImageView);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        addressTextView = (TextView) view.findViewById(R.id.addressTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);

        updateMyProfileButton = (Button) view.findViewById(R.id.updateProfileButton);
        updateMyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.changeFragment(UpdateProfileFragment.newInstance(MyProfileFragment.this));
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
    public void init() {
        appManager = AppManager.getInstance(parentActivity);
        setupProfile();
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle("My Profile");
    }

    private void setupProfile() {
        User user = appManager.getUser();
        if ( user != null ) {
            WebImageCache imageCache = new WebImageCache(parentActivity);
            final Bitmap bitmap = imageCache.get(user.getUserPicture());
            if ( bitmap != null ) {
                avatarImageView.setImageBitmap(bitmap);
            }
            else {
                avatarImageView.setImageUrl(user.getUserPicture(), new SmartImageTask.OnCompleteListener() {
                    @Override
                    public void onComplete(Bitmap bitmap1) {
                        if ( bitmap1 != null )
                            avatarImageView.setImageBitmap(bitmap1);
                    }
                    @Override
                    public void onComplete() {

                    }
                });
            }

            nameTextView.setText(user.getUserFirstName() + " " + user.getUserLastName());
            String address = user.getUserAddress1() + "\n" + user.getUserAddress2() + "\n" + user.getUserCity();
            if ( user.getUserCity() != null && !user.getUserCity().isEmpty() &&
                    user.getUserState() != null && !user.getUserState().isEmpty() ) {
                address += ", " + user.getUserState();
            }
            address += "\n" + user.getUserZip() + "\n" + user.getUserCountry();
            addressTextView.setText(address);
            phoneTextView.setText(user.getUserPhone1() + "\n" + user.getUserPhone2());
            emailTextView.setText(user.getUserEmail());
        }
    }

    @Override
    public void didUpdateUserProfile() {
        setupProfile();
        parentActivity.getMenuFragment().setUserInfo();
    }
}
