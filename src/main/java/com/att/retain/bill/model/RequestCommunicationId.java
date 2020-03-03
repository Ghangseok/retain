package com.att.retain.bill.model;

import java.io.Serializable;

public class RequestCommunicationId implements Serializable {

    private String reqDate;
    private Integer reqOrdNum;
    private Integer pageNum;

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public Integer getReqOrdNum() {
        return reqOrdNum;
    }

    public void setReqOrdNum(Integer reqOrdNum) {
        this.reqOrdNum = reqOrdNum;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RequestCommunicationId{");
        sb.append("reqDate='").append(reqDate).append('\'');
        sb.append(", reqOrdNum=").append(reqOrdNum);
        sb.append(", pageNum=").append(pageNum);
        sb.append('}');
        return sb.toString();
    }
}
