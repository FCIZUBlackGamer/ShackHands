package com.shahen.shackhands.APIS.WebServiceConnection;

import com.shahen.shackhands.APIS.MenuModel;

import java.util.List;

public class ResponseModel {
    List<MenuModel> Menu;

    public void setMenu(List<MenuModel> menu) {
        Menu = menu;
    }

    public List<MenuModel> getMenu() {
        return Menu;
    }
}
