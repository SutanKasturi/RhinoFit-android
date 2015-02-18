package com.travis.rhinofit.models;

import com.travis.rhinofit.fragments.MenuFragment;
import com.travis.rhinofit.fragments.MenuFragment.MenuIds;

public class MenuItem {
    MenuIds menuId;
	String title;

	public MenuItem(MenuIds id, String title) {
        this.menuId = id;
		this.title = title;
	}

    public MenuIds getMenuId() {
        return menuId;
    }

    public void setMenuId(MenuIds menuId) {
        this.menuId = menuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
