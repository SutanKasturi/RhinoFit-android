package com.travis.rhinofit.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.soundcloud.android.crop.Crop;

import java.io.File;

/**
 * Created by admin on 2/18/15.
 */
public class BaseImageChooserFragment extends BaseFragment implements ImageChooserListener {

    private ImageChooserManager imageChooserManager;

    public String filePath;
    private int chooserType;

    public boolean isCrop;
    public boolean isSquare;

    @Override
    public void setNavTitle() {

    }

    @Override
    public void init() {

    }

    public void showDialogSelectPhoto() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    takePicture();
                } else if (items[item].equals("Choose from Library")) {
                    chooseImage();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                chooserType, "rhinofit", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                chooserType, "rhinofit", true);
        imageChooserManager.setImageChooserListener(this);
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK ) {
            if (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)
            {
                if (imageChooserManager == null) {
                    reinitializeImageChooser();
                }
                imageChooserManager.submit(requestCode, data);
            }
            else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(data);
            }
        } else {

        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        parentActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {
                    filePath = image.getFileThumbnail();
                    cropImage(Uri.parse(new File(filePath).toString()));
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        parentActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(parentActivity, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "rhinofit", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    public void cropImage(Uri imageUri){
        Uri uriFromPath = Uri.fromFile(new File(imageUri.getPath()));
        if ( isCrop ) {
            if ( isSquare ) {
                new Crop(uriFromPath).output(uriFromPath).asSquare().start(parentActivity);
            }
            else {
                new Crop(uriFromPath).output(uriFromPath).start(parentActivity);
            }
        }
        else {
            setImage(uriFromPath);
        }
    }

    public void handleCrop(Intent result) {
        setImage(Crop.getOutput(result));
    }
    public void setImage(Uri imageUri) {

    }
}
