package com.att.retain.bill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TB_REQ_STAT")
@IdClass(RequestStatisticsId.class)
public class RequestStatistics implements Serializable {

    @Id
    @Column(name="REQ_DATE", nullable = false)
    private String reqDate;
    @Id
    @Column(name="REQ_ORD_NUM", nullable = false)
    private int reqOrdNum;
    @Column(name="PAGE_SIZE")
    private int pageSize;
    @Column(name="THREAD_COUNT")
    private int threadCount;
    @Column(name="RETRY_TIMES")
    private int retryTimes;
    @Column(name="RETRY_WAIT_MSEC")
    private int retryWaitMsec;
    @Column(name="SUBMIT_TRANSACTIONS")
    private int submitTransactions;
    @Column(name="SUBMISSION_FAILURES")
    private int submissionFailues;
    @Column(name="START_TIME_PROC")
    private String startTimeProc;
    @Column(name="END_TIME_PROC")
    private String endTimeProc;
    @Column(name="CREATED_DTTM", nullable = false)
    private Date createdDttm;

}
