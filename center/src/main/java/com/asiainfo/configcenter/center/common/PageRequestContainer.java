package com.asiainfo.configcenter.center.common;

import java.io.Serializable;

/**
 * 前端页面请求参数容器类
 * Created by bawy on 2018/7/3.
 */
public class PageRequestContainer<T> implements Serializable {

    private static final long serialVersionUID = 1874070126728105882L;

    private int pageSize;
    private int currentPage;
    private T data;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int curentPage) {
        this.currentPage = curentPage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
