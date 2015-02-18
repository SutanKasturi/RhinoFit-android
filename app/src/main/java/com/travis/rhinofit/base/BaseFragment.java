package com.travis.rhinofit.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.travis.rhinofit.MainActivity;
import com.travis.rhinofit.R;
import com.travis.rhinofit.http.CustomAsyncHttpRequest;
import com.travis.rhinofit.utils.image.SmartImageView;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public abstract class BaseFragment extends Fragment {
    public MainActivity parentActivity;
    public CustomAsyncHttpRequest task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity)getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onDestroy() {
        if ( task != null )
        {
            task.onCancel();
            task = null;
        }
        super.onDestroy();
    }
    public void setNavTitle(String title) {
        if ( parentActivity != null )
            parentActivity.setNavTitle(title);
    }
    public abstract void setNavTitle();
    public abstract void init();
}
