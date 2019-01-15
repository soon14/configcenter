package com.asiainfo.configcenter.center.vo.task;

/**
 * 配置变更详情VO对象
 * Created by bawy on 2018/8/27 17:09.
 */
public class ConfigChangeDetailVO {

    private String oldContent;
    private String newContent;

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }
}
