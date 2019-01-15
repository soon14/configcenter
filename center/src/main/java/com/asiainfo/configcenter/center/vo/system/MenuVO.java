package com.asiainfo.configcenter.center.vo.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单树
 * Created by bawy on 2018/7/16 22:40.
 */
public class MenuVO implements Serializable {


    private static final long serialVersionUID = 4426726252335794863L;

    private int menuId;
    private String menuName;
    private byte menuLevel;
    private String menuLink;
    private String menuIcon;
    private String description;
    private boolean isLeaf;
    private List<MenuVO> childrenMenu;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public MenuVO() {
        childrenMenu = new ArrayList<>();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public byte getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(byte menuLevel) {
        this.menuLevel = menuLevel;
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public void addChildMenu(MenuVO menuVO){
        childrenMenu.add(menuVO);
    }

    public List<MenuVO> getChildrenMenu() {
        return childrenMenu;
    }

    public void setChildrenMenu(List<MenuVO> childrenMenu) {
        this.childrenMenu = childrenMenu;
    }
}
