package com.travis.rhinofit.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.travis.rhinofit.LoginActivity;
import com.travis.rhinofit.MainActivity;
import com.travis.rhinofit.R;
import com.travis.rhinofit.adapter.MenuArrayAdapter;
import com.travis.rhinofit.base.BaseFragment;
import com.travis.rhinofit.global.AppManager;
import com.travis.rhinofit.models.MenuItem;
import com.travis.rhinofit.models.User;
import com.travis.rhinofit.utils.image.SmartImageTask;
import com.travis.rhinofit.utils.image.SmartImageView;
import com.travis.rhinofit.utils.image.WebImageCache;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sutan Kasturi on 2/9/15.
 */
public class MenuFragment extends Fragment {

    public enum MenuIds {
        MY_CLASSES,
        MY_RESERVATIONS,
        MY_ATTENDANCE,
        MY_WODS,
        MY_BENCHMARKS,
        MY_MEMBERSHIPS,
        THE_WALL,
        MY_PROFILE,
        LOG_OUT
    }

    Context context;

    SmartImageView      userPhotoImageView;
    TextView            userNameTextView;
    ListView            menuListView;
    MenuArrayAdapter    menuArrayAdapter;

    private static MenuIds currentMenu = MenuIds.MY_CLASSES;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_frame, container, false);

        context = getActivity();

        userPhotoImageView = (SmartImageView) view.findViewById(R.id.menuUserPhotoImage);
        userNameTextView = (TextView) view.findViewById(R.id.menuUserNameTextView);
        menuListView = (ListView) view.findViewById(R.id.menuListView);

        User user = AppManager.getInstance(getActivity()).getUser();
        if ( user != null ) {
            WebImageCache imageCache = new WebImageCache(getActivity());
            final Bitmap bitmap = imageCache.get(user.getUserPicture());
            if ( bitmap != null ) {
                userPhotoImageView.setImageBitmap(bitmap);
            }
            else {
                userPhotoImageView.setImageUrl(user.getUserPicture(), new SmartImageTask.OnCompleteListener() {
                    @Override
                    public void onComplete(Bitmap bitmap1) {
                        if ( bitmap1 != null)
                            userPhotoImageView.setImageBitmap(bitmap1);
                    }
                    @Override
                    public void onComplete() {
                    }
                });
            }
            userNameTextView.setText(user.getUserEmail());
        }

        setupActions();
        setupMenu();

        return view;
    }

    private void setupActions() {
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = menuArrayAdapter.getItem(position);
                if ( currentMenu == item.getMenuId() ) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.toggle();
                }
                else {
                    if ( item.getMenuId() == MenuIds.LOG_OUT ) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);

                        ((MainActivity)context).finish();
                    }
                    else {
                        switchFragment(getFragment(item.getMenuId()));
                    }
                }
            }
        });
    }

    private void setupMenu() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem(MenuIds.MY_CLASSES, "My Classes"));
        menuItems.add(new MenuItem(MenuIds.MY_RESERVATIONS, "My Reservations"));
        menuItems.add(new MenuItem(MenuIds.MY_ATTENDANCE, "My Attendance"));
        menuItems.add(new MenuItem(MenuIds.MY_WODS, "My WODS"));
        menuItems.add(new MenuItem(MenuIds.MY_BENCHMARKS, "My Benchmarks"));
        menuItems.add(new MenuItem(MenuIds.MY_MEMBERSHIPS, "My Memberships"));
        menuItems.add(new MenuItem(MenuIds.THE_WALL, "The Wall"));
        menuItems.add(new MenuItem(MenuIds.MY_PROFILE, "My Profile"));
        menuItems.add(new MenuItem(MenuIds.LOG_OUT, "Log out"));

        menuArrayAdapter = new MenuArrayAdapter(context, R.layout.row_menu, menuItems);
        menuListView.setAdapter(menuArrayAdapter);
    }

    public static void setCurrentMenu(MenuIds menuId) {
        currentMenu = menuId;
    }

    public static BaseFragment getFragment() {
        return getFragment(currentMenu);
    }

    public static BaseFragment getFragment(MenuIds menuId) {
        currentMenu = menuId;
        if ( currentMenu == MenuIds.MY_CLASSES ) {
            return new MyClassesFragment();
        }
        else if ( currentMenu == MenuIds.MY_RESERVATIONS) {
            return new MyReservationsFragment();
        }
        else if ( currentMenu == MenuIds.MY_ATTENDANCE ) {
            return new MyAttendanceFragment();
        }
        else if ( currentMenu == MenuIds.MY_WODS ) {
            return MyWODsFragment.newInstance(new Date());
        }
        else if ( currentMenu == MenuIds.MY_BENCHMARKS ) {
            return new MyBenchmarksFragment();
        }
        else if ( currentMenu == MenuIds.MY_MEMBERSHIPS ) {
            return new MyMembershipsFragment();
        }
        else if ( currentMenu == MenuIds.THE_WALL ) {
            return new TheWallFragment();
        }
        else if ( currentMenu == MenuIds.MY_PROFILE ) {
            return new MyProfileFragment();
        }

        return null;
    }

    private void switchFragment(final BaseFragment fragment) {
        if ( context == null || fragment == null )
            return;

        if ( getActivity() instanceof MainActivity ) {
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.switchContent(fragment);
        }
    }
}
