package com.asiainfo.configcenter.center.vo.configpush;

/**
 * 因为推送详情中可能没有填写推送信息 需要从任务的扩展信息表中获取到推送信息
 * 获取到推送信息之后放到当前的VO中
 */
public class PushRuleVO {
    private int [] instanceIds;
    private String cornTime;
    private String desc;

    public int[] getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(int[] instanceIds) {
        this.instanceIds = instanceIds;
    }

    public String getCornTime() {
        return cornTime;
    }

    public void setCornTime(String cornTime) {
        this.cornTime = cornTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
