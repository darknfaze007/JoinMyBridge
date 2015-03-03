package com.misczak.joinmybridge;

/**
 * Created by misczak on 3/2/15.
 */
public class DrawerItem {

    int drawerItemIconId;
    String drawerItemTitle;

    public int getMenuItemId() {
        return drawerItemIconId;
    }

    public void setMenuItemId(int menuItemId) {
        this.drawerItemIconId = menuItemId;
    }

    public String getMenuItemTitle() {
        return drawerItemTitle;
    }

    public void setMenuItemTitle(String menuItemTitle) {
        this.drawerItemTitle = menuItemTitle;
    }

}
