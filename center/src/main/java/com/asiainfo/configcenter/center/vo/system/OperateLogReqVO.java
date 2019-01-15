package com.asiainfo.configcenter.center.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by oulc on 2018/7/20.
 */
public class OperateLogReqVO {
    private int userId;
    private long startDate;
    private long endDate;
    private byte operateType;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public byte getOperateType() {
        return operateType;
    }

    public void setOperateType(byte operateType) {
        this.operateType = operateType;
    }
}
