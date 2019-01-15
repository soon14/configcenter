package com.asiainfo.configcenter.center.common;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 前端页面请求结果容器类
 * Created by bawy on 2018/7/3.
 */
public class PageResultContainer<T> implements Serializable {

    private static final long serialVersionUID = -6697572120265371577L;

    private long count;
    private List<T> entities;

    public PageResultContainer(){

    }

    public PageResultContainer(Page<T> page){
        count = page.getTotalElements();
        entities = page.getContent();
    }

    public PageResultContainer(List<T> entities,int count){
        this.entities = entities;
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }
}
