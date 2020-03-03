package com.att.retain.bill.model;

import java.io.Serializable;

public class RequestTransactionId implements Serializable {

    private String reqDate;
    private Integer reqOrdNum;
    private String merchantTransactionId;

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

    public String getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public void setMerchantTransactionId(String merchantTransactionId) {
        this.merchantTransactionId = merchantTransactionId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RequestTransactionId{");
        sb.append("reqDate='").append(reqDate).append('\'');
        sb.append(", reqOrdNum=").append(reqOrdNum);
        sb.append(", merchantTransactionId='").append(merchantTransactionId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
