package com.att.retain.bill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="TB_REQ_COMM")
@IdClass(RequestCommunicationId.class)
public class RequestCommunication implements Serializable {

    @Id
    @Column(name="REQ_DATE", nullable = false)
    private String reqDate;
    @Id
    @Column(name="REQ_ORD_NUM", nullable = false)
    private Integer reqOrdNum;
    @Id
    @Column(name="PAGE_NUM", nullable = false)
    private Integer pageNum;
    @Column(name="SOAP_ID")
    private String soapId;
    @Column(name="STATUS")
    private String status;
    @Column(name="MSG")
    private String msg;
    @Column(name="SUBMIT_DTTM")
    private String submitDttm;
    @Column(name="CREATED_DTTM")
    private String createdDttm;

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

    public String getSoapId() {
        return soapId;
    }

    public void setSoapId(String soapId) {
        this.soapId = soapId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubmitDttm() {
        return submitDttm;
    }

    public void setSubmitDttm(String submitDttm) {
        this.submitDttm = submitDttm;
    }

    public String getCreatedDttm() {
        return createdDttm;
    }

    public void setCreatedDttm(String createdDttm) {
        this.createdDttm = createdDttm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestCommunication)) return false;
        RequestCommunication that = (RequestCommunication) o;
        return getReqDate().equals(that.getReqDate()) &&
                getReqOrdNum().equals(that.getReqOrdNum()) &&
                getPageNum().equals(that.getPageNum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReqDate(), getReqOrdNum(), getPageNum());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RequestCommunication{");
        sb.append("reqDate='").append(reqDate).append('\'');
        sb.append(", reqOrdNum=").append(reqOrdNum);
        sb.append(", pageNum=").append(pageNum);
        sb.append(", soapId='").append(soapId).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", submitDttm=").append(submitDttm);
        sb.append(", createdDttm=").append(createdDttm);
        sb.append('}');
        return sb.toString();
    }
}
