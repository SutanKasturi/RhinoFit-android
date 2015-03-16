package com.travis.rhinofit.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.soundcloud.android.crop.Crop;
import com.travis.rhinofit.R;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.base.BaseImageChooserFragment;
import com.travis.rhinofit.customs.CustomEditText;
import com.travis.rhinofit.global.AppManager;
import com.travis.rhinofit.global.Constants;
import com.travis.rhinofit.http.CustomAsyncHttpRequest;
import com.travis.rhinofit.http.WebService;
import com.travis.rhinofit.listener.InterfaceHttpRequest;
import com.travis.rhinofit.models.JSONModel;
import com.travis.rhinofit.models.User;
import com.travis.rhinofit.utils.AlertUtil;
import com.travis.rhinofit.utils.image.SmartImageTask;
import com.travis.rhinofit.utils.image.SmartImageView;
import com.travis.rhinofit.utils.image.WebImageCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2/19/15.
 */
public class UpdateProfileFragment extends BaseImageChooserFragment {

    private static ArrayList<String> countries;
    private static Map<String, ArrayList<String>> states;

    Bitmap userImage;

    SmartImageView avatarImageView;
    CustomEditText firstNameTextView;
    CustomEditText lastNameTextView;
    CustomEditText address1EditText;
    CustomEditText address2EditText;
    CustomEditText cityEditText;
    Spinner stateSpinner;
    CustomEditText postalEditText;
    Spinner countrySpinner;
    CustomEditText homePhoneEditText;
    CustomEditText mobilePhoneEditText;
    CustomEditText emailEditText;

    Button saveButton;
    AppManager appManager;
    User user;

    UpdateProfileListener listener;

    CustomAsyncHttpRequest stateTask = null;

    public static UpdateProfileFragment newInstance(UpdateProfileListener callback) {
        UpdateProfileFragment updateProfileFragment = new UpdateProfileFragment();
        updateProfileFragment.listener = callback;
        return updateProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_profile_frame, container, false);

        avatarImageView = (SmartImageView) view.findViewById(R.id.avatarImageView);
        firstNameTextView = (CustomEditText) view.findViewById(R.id.firstNameEditText);
        lastNameTextView = (CustomEditText) view.findViewById(R.id.lastNameEditText);
        address1EditText = (CustomEditText) view.findViewById(R.id.address1EditText);
        address2EditText = (CustomEditText) view.findViewById(R.id.address2EditText);
        cityEditText = (CustomEditText) view.findViewById(R.id.cityEditText);
        stateSpinner = (Spinner) view.findViewById(R.id.stateSpinner);
        postalEditText = (CustomEditText) view.findViewById(R.id.postalEditText);
        countrySpinner = (Spinner) view.findViewById(R.id.countrySpinner);
        homePhoneEditText = (CustomEditText) view.findViewById(R.id.homePhoneEditText);
        mobilePhoneEditText = (CustomEditText) view.findViewById(R.id.mobilePhoneEditText);
        emailEditText = (CustomEditText) view.findViewById(R.id.emailEditText);

        address1EditText.setType(CustomEditText.NONE);
        address2EditText.setType(CustomEditText.NONE);
        cityEditText.setType(CustomEditText.NONE);
        postalEditText.setType(CustomEditText.NONE);
        homePhoneEditText.setType(CustomEditText.NONE);
        mobilePhoneEditText.setType(CustomEditText.NONE);
        emailEditText.setType(CustomEditText.EMAIL);

        avatarImageView.setClickable(true);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSelectPhoto();
            }
        });

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setStateSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
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
        user = appManager.getUser();
        setupProfile();
        setCountySpinner();
        isCrop = true;
        isSquare = true;
    }

    @Override
    public void setNavTitle() {
        parentActivity.setNavTitle("Update Profile");
    }

    private void setupProfile() {
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

            firstNameTextView.setText(user.getUserFirstName());
            lastNameTextView.setText(user.getUserLastName());
            address1EditText.setText(user.getUserAddress1());
            address2EditText.setText(user.getUserAddress2());
            cityEditText.setText(user.getUserCity());
            postalEditText.setText(user.getUserZip());
            homePhoneEditText.setText(user.getUserPhone1());
            mobilePhoneEditText.setText(user.getUserPhone2());
            emailEditText.setText(user.getUserEmail());
        }
    }

    private void setCountySpinner() {
        if ( countries != null ) {
            countrySpinner.setEnabled(true);
            ArrayAdapter countryAdapter = new ArrayAdapter(parentActivity, android.R.layout.simple_spinner_item, countries);
            countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countrySpinner.setAdapter(countryAdapter);

            if ( user.getUserCountry() != null && !user.getUserCountry().isEmpty() ) {
                for ( int i = 0; i < countries.size(); i++ ) {
                    String country = countries.get(i);
                    if ( country.equals(user.getUserCountry()) ) {
                        countrySpinner.setSelection(i, true);
                        setStateSpinner();
                        break;
                    }
                }
            }
        }
        else {
            countrySpinner.setEnabled(false);
            countrySpinner.setAdapter(null);
            stateSpinner.setEnabled(false);
            stateSpinner.setAdapter(null);
            WebService.getCountries(parentActivity, new InterfaceHttpRequest.HttpRequestArrayListener() {
                @Override
                public void complete(JSONArray result, String errorMsg) {
                    if ( result != null ) {
                        countries = new ArrayList<String>();
                        for ( int i = 0; i < result.length(); i++ ) {
                            try {
                                countries.add(result.getString(i));
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        setCountySpinner();
                    }
                    else {
                        if ( errorMsg == null )
                            errorMsg = "Failure get countries";
                        AlertUtil.messageAlert(parentActivity, "Error", errorMsg);
                    }
                }
            }).onRun();
        }
    }


    private void setStateSpinner() {
        stateSpinner.setEnabled(false);
        stateSpinner.setAdapter(null);
        final String country = countrySpinner.getSelectedItem().toString();

        if ( countries == null || countries.size() <=0 || country == null || country.isEmpty() )
            return;

        if ( states == null ) {
            states = new HashMap<String, ArrayList<String>>();
        }
        else {
            if ( states.get(country) != null ) {
                ArrayList<String> currentStates = new ArrayList<String>();
                currentStates = states.get(country);

                stateSpinner.setEnabled(true);
                ArrayAdapter stateAdapter = new ArrayAdapter(parentActivity, android.R.layout.simple_spinner_item, currentStates);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stateSpinner.setAdapter(stateAdapter);

                if ( user.getUserCountry().equals(country) ) {
                    for ( int i = 0; i < currentStates.size(); i++ ) {
                        String state = currentStates.get(i);
                        if ( state.equals(user.getUserState()) ) {
                            stateSpinner.setSelection(i, true);
                        }
                    }
                }

                return;
            }
        }
        if ( stateTask != null ) {
            stateTask.onCancel();
            stateTask = null;
        }
        stateTask = WebService.getStates(parentActivity, country, new InterfaceHttpRequest.HttpRequestArrayListener() {
            @Override
            public void complete(JSONArray result, String errorMsg) {
                if ( result != null ) {
                    ArrayList<String> currentStates = new ArrayList<String>();
                    for ( int i = 0; i < result.length(); i++ ) {
                        try {
                            currentStates.add(result.getString(i));
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    states.put(country, currentStates);
                    setStateSpinner();
                }
                else {
                    if ( errorMsg == null )
                        errorMsg = "Failure get countries";
                    AlertUtil.messageAlert(parentActivity, "Error", errorMsg);
                }
            }
        });
        stateTask.onRun();
    }

    private void onSave() {
        boolean isValidate = true;

        if ( !firstNameTextView.isValidInput() )
            isValidate = false;
        if ( !lastNameTextView.isValidInput() )
            isValidate = false;
//        if ( !address1EditText.isValidInput() )
//            isValidate = false;
//        if ( !cityEditText.isValidInput() )
//            isValidate = false;
//        if ( stateSpinner.isEnabled() && stateSpinner.getSelectedItem() == null )
//            isValidate = false;
//        if ( countrySpinner.isEnabled() && countrySpinner.getSelectedItem() == null )
//            isValidate = false;
//        if ( !postalEditText.isValidInput() )
//            isValidate = false;
//        if ( !homePhoneEditText.isValidInput() )
//            isValidate = false;
//        if ( !mobilePhoneEditText.isValidInput() )
//            isValidate = false;
        if ( !emailEditText.isValidInput() )
            isValidate = false;

        if ( isValidate == false )
            return;

        final ProgressDialog progressDialog = new ProgressDialog(parentActivity);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        WebService.updateUserInfo(parentActivity,
                Constants.kParamFile,
                userImage,
                filePath,
                firstNameTextView.getText().toString(),
                lastNameTextView.getText().toString(),
                address1EditText.getText().toString(),
                address2EditText.getText().toString(),
                cityEditText.getText().toString(),
                stateSpinner.getSelectedItem().toString(),
                postalEditText.getText().toString(),
                countrySpinner.getSelectedItem().toString(),
                homePhoneEditText.getText().toString(),
                mobilePhoneEditText.getText().toString(),
                emailEditText.getText().toString(),
                new InterfaceHttpRequest.HttpRequestJsonListener() {
                    @Override
                    public void complete(JSONObject result, String errorMsg) {
                        progressDialog.dismiss();
                        if ( result != null && !JSONModel.isNull(result, "success") && JSONModel.getIntFromJson(result, "success") == 1 ) {
                            WebImageCache imageCache = new WebImageCache(parentActivity);
                            imageCache.remove(user.getUserPicture());

                            user.setUserFirstName(firstNameTextView.getText().toString());
                            user.setUserLastName(lastNameTextView.getText().toString());
                            user.setUserAddress1(address1EditText.getText().toString());
                            user.setUserAddress2(address2EditText.getText().toString());
                            user.setUserCity(cityEditText.getText().toString());
                            user.setUserState(stateSpinner.getSelectedItem().toString());
                            user.setUserZip(postalEditText.getText().toString());
                            user.setUserCountry(countrySpinner.getSelectedItem().toString());
                            user.setUserPhone1(homePhoneEditText.getText().toString());
                            user.setUserPhone2(mobilePhoneEditText.getText().toString());
                            user.setUserEmail(emailEditText.getText().toString());
                            user.setUserPicture(JSONModel.getStringFromJson(result, "photo"));

                            AppManager.getInstance(parentActivity).setUser(user);
                            if ( listener != null )
                                listener.didUpdateUserProfile();
                            parentActivity.onBackPressed();
                        }
                        else {
                            if ( errorMsg == null || errorMsg.isEmpty() )
                                errorMsg = "Failure update user info";
                            AlertUtil.messageAlert(parentActivity, "Error", errorMsg);
                        }
                    }
                }).onRun();
    }

    @Override
    public void setImage(Uri imageUri) {
//        Uri uriFromPath = Uri.fromFile(new File(imageUri.getPath()));
        try {
            userImage = BitmapFactory.decodeStream(parentActivity.getContentResolver().openInputStream(imageUri));
            avatarImageView.setImageBitmap(userImage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public interface UpdateProfileListener {
        public void didUpdateUserProfile();
    }
}
