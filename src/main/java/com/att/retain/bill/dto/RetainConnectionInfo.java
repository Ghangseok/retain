package com.att.retain.bill.dto;

import java.util.Objects;

public class RetainConnectionInfo {
    private String endpoint;
    private String username;
    private String passowrd;
    private String version;
    private String userAgent;

    private Integer timeOutInMilliSeconds;
    private Integer retryTimes;
    private Integer retryWaitingMsec;

    public RetainConnectionInfo(String endpoint, String username, String passowrd, String version, String userAgent,
                                Integer timeOutInMilliSeconds, Integer retryTimes, Integer retryWaitingMsec) {
        this.endpoint = endpoint;
        this.username = username;
        this.passowrd = passowrd;
        this.version = version;
        this.userAgent = userAgent;
        this.timeOutInMilliSeconds = timeOutInMilliSeconds;
        this.retryTimes = retryTimes;
        this.retryWaitingMsec = retryWaitingMsec;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Integer getTimeOutInMilliSeconds() {
        return timeOutInMilliSeconds;
    }

    public void setTimeOutInMilliSeconds(Integer timeOutInMilliSeconds) {
        this.timeOutInMilliSeconds = timeOutInMilliSeconds;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getRetryWaitingMsec() {
        return retryWaitingMsec;
    }

    public void setRetryWaitingMsec(Integer retryWaitingMsec) {
        this.retryWaitingMsec = retryWaitingMsec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RetainConnectionInfo)) return false;
        RetainConnectionInfo that = (RetainConnectionInfo) o;
        return getEndpoint().equals(that.getEndpoint()) &&
                getUsername().equals(that.getUsername()) &&
                getPassowrd().equals(that.getPassowrd()) &&
                Objects.equals(getVersion(), that.getVersion()) &&
                Objects.equals(getUserAgent(), that.getUserAgent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEndpoint(), getUsername(), getPassowrd(), getVersion(), getUserAgent());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RetainConnectionInfo{");
        sb.append("endpoint='").append(endpoint).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", passowrd='").append(passowrd).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append(", timeOutInMilliSeconds=").append(timeOutInMilliSeconds);
        sb.append(", retryTimes=").append(retryTimes);
        sb.append(", retryWaitingMsec=").append(retryWaitingMsec);
        sb.append('}');
        return sb.toString();
    }
}
