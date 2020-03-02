package com.att.retain.bill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "TB_REQ_TX")
@IdClass(RequestTransactionId.class)
public class RequestTransaction implements Serializable {

    @Id
    @Column(name="REQ_DATE", nullable = false)
    private String reqDate;
    @Id
    @Column(name="REQ_ORD_NUM", nullable = false)
    private int reqOrdNum;
    @Id
    @Column(name="MERCHANT_TRANSACTION_ID", nullable = false)
    private String merchantTransactionId;
    @Column(name="VID")
    private String vid;
    @Column(name="TIMESTAMP")
    private String timestamp;
    @Column(name="AMOUNT")
    private String amount;
    @Column(name="CURRENCY")
    private String currency;
    @Column(name="STATUS")
    private String status;
    @Column(name="DIVISION_NUMBER")
    private String divisionNumber;
    @Column(name="SELECT_TRANSACTION_ID")
    private String selectTransactionId;
    @Column(name="SELECT_REFUND_ID")
    private String selectRefundId;
    @Column(name="SUBSCRIPTION_ID", nullable = false)
    private String subscriptionId;
    @Column(name="SUBSCRIPTION_START_DATE")
    private String subscriptionStartDate;
    @Column(name="BILLING_FREQUENCY")
    private String billingFrequency;
    @Column(name="PREVIOUS_BILLING_DATE")
    private String previousBillingDate;
    @Column(name="PREVIOUS_BILLING_COUNT")
    private String previousBillingCount;
    @Column(name="CUSTOMER_ID", nullable = false)
    private String customerId;
    @Column(name="PAYMENT_METHOD_ID")
    private String paymentMethodId;
    @Column(name="PAYMENT_METHOD_IS_TOKENIZED")
    private String paymentMethodIsTokenized;
    @Column(name="CREDIT_CARD_ACCOUNT")
    private String creditCardAccount;
    @Column(name="CREDIT_CARD_ACCOUNT_HASH")
    private String creditCardAccountHash;
    @Column(name="CREDIT_CARD_EXPIRATION_DATE")
    private String creditCardExpirationDate;
    @Column(name="ACCOUNT_HOLDER_NAME")
    private String accountHolderName;
    @Column(name="BILLING_ADDRESS_LINE1")
    private String billingAddressLine1;
    @Column(name="BILLING_ADDRESS_LINE2")
    private String billingAddressLine2;
    @Column(name="BILLING_ADDRESS_LINE3")
    private String billingAddressLine3;
    @Column(name="BILLING_ADDRESS_CITY")
    private String billingAddressCity;
    @Column(name="BILLING_ADDRESS_COUNTY")
    private String billingAddressCounty;
    @Column(name="BILLING_ADDRESS_DISTRICT")
    private String billingAddressDistrict;
    @Column(name="BILLING_ADDRESS_POSTALCODE")
    private String billingAddressPostalcode;
    @Column(name="BILLING_ADDRESS_COUNTRY")
    private String billingAddressCountry;
    @Column(name="NAME_VALUES")
    private String nameValues;
    @Column(name="AFFILIATE_ID")
    private String affiliateId;
    @Column(name="AFFILIATE_SUB_ID")
    private String affiliateSubId;
    @Column(name="BILLING_STATEMENT_IDENTIFIER")
    private String billingStatementIdentifier;
    @Column(name="AUTH_CODE")
    private String authCode;
    @Column(name="AVS_CODE")
    private String avsCode;
    @Column(name="CVN_CODE")
    private String cvnCode;
    @Column(name="CREDIT_CARD_ACCOUNT_UPDATED")
    private String creditCardAccountUpdated;
    @Column(name="IS_READ", nullable = false)
    private String isRead;
    @Column(name="READ_DTTM")
    private Date readDttm;
    @Column(name="PAGE_NUM")
    private int pageNum;
    @Column(name="RETURN_CODE")
    private String returnCode;
    @Column(name="RETURN_MSG")
    private String returnMsg;
    @Column(name="CREATED_DTTM", nullable = false)
    private Date createdDttm;

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public int getReqOrdNum() {
        return reqOrdNum;
    }

    public void setReqOrdNum(int reqOrdNum) {
        this.reqOrdNum = reqOrdNum;
    }

    public String getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public void setMerchantTransactionId(String merchantTransactionId) {
        this.merchantTransactionId = merchantTransactionId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDivisionNumber() {
        return divisionNumber;
    }

    public void setDivisionNumber(String divisionNumber) {
        this.divisionNumber = divisionNumber;
    }

    public String getSelectTransactionId() {
        return selectTransactionId;
    }

    public void setSelectTransactionId(String selectTransactionId) {
        this.selectTransactionId = selectTransactionId;
    }

    public String getSelectRefundId() {
        return selectRefundId;
    }

    public void setSelectRefundId(String selectRefundId) {
        this.selectRefundId = selectRefundId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(String subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public String getBillingFrequency() {
        return billingFrequency;
    }

    public void setBillingFrequency(String billingFrequency) {
        this.billingFrequency = billingFrequency;
    }

    public String getPreviousBillingDate() {
        return previousBillingDate;
    }

    public void setPreviousBillingDate(String previousBillingDate) {
        this.previousBillingDate = previousBillingDate;
    }

    public String getPreviousBillingCount() {
        return previousBillingCount;
    }

    public void setPreviousBillingCount(String previousBillingCount) {
        this.previousBillingCount = previousBillingCount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodIsTokenized() {
        return paymentMethodIsTokenized;
    }

    public void setPaymentMethodIsTokenized(String paymentMethodIsTokenized) {
        this.paymentMethodIsTokenized = paymentMethodIsTokenized;
    }

    public String getCreditCardAccount() {
        return creditCardAccount;
    }

    public void setCreditCardAccount(String creditCardAccount) {
        this.creditCardAccount = creditCardAccount;
    }

    public String getCreditCardAccountHash() {
        return creditCardAccountHash;
    }

    public void setCreditCardAccountHash(String creditCardAccountHash) {
        this.creditCardAccountHash = creditCardAccountHash;
    }

    public String getCreditCardExpirationDate() {
        return creditCardExpirationDate;
    }

    public void setCreditCardExpirationDate(String creditCardExpirationDate) {
        this.creditCardExpirationDate = creditCardExpirationDate;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getBillingAddressLine1() {
        return billingAddressLine1;
    }

    public void setBillingAddressLine1(String billingAddressLine1) {
        this.billingAddressLine1 = billingAddressLine1;
    }

    public String getBillingAddressLine2() {
        return billingAddressLine2;
    }

    public void setBillingAddressLine2(String billingAddressLine2) {
        this.billingAddressLine2 = billingAddressLine2;
    }

    public String getBillingAddressLine3() {
        return billingAddressLine3;
    }

    public void setBillingAddressLine3(String billingAddressLine3) {
        this.billingAddressLine3 = billingAddressLine3;
    }

    public String getBillingAddressCity() {
        return billingAddressCity;
    }

    public void setBillingAddressCity(String billingAddressCity) {
        this.billingAddressCity = billingAddressCity;
    }

    public String getBillingAddressCounty() {
        return billingAddressCounty;
    }

    public void setBillingAddressCounty(String billingAddressCounty) {
        this.billingAddressCounty = billingAddressCounty;
    }

    public String getBillingAddressDistrict() {
        return billingAddressDistrict;
    }

    public void setBillingAddressDistrict(String billingAddressDistrict) {
        this.billingAddressDistrict = billingAddressDistrict;
    }

    public String getBillingAddressPostalcode() {
        return billingAddressPostalcode;
    }

    public void setBillingAddressPostalcode(String billingAddressPostalcode) {
        this.billingAddressPostalcode = billingAddressPostalcode;
    }

    public String getBillingAddressCountry() {
        return billingAddressCountry;
    }

    public void setBillingAddressCountry(String billingAddressCountry) {
        this.billingAddressCountry = billingAddressCountry;
    }

    public String getNameValues() {
        return nameValues;
    }

    public void setNameValues(String nameValues) {
        this.nameValues = nameValues;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public String getAffiliateSubId() {
        return affiliateSubId;
    }

    public void setAffiliateSubId(String affiliateSubId) {
        this.affiliateSubId = affiliateSubId;
    }

    public String getBillingStatementIdentifier() {
        return billingStatementIdentifier;
    }

    public void setBillingStatementIdentifier(String billingStatementIdentifier) {
        this.billingStatementIdentifier = billingStatementIdentifier;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAvsCode() {
        return avsCode;
    }

    public void setAvsCode(String avsCode) {
        this.avsCode = avsCode;
    }

    public String getCvnCode() {
        return cvnCode;
    }

    public void setCvnCode(String cvnCode) {
        this.cvnCode = cvnCode;
    }

    public String getCreditCardAccountUpdated() {
        return creditCardAccountUpdated;
    }

    public void setCreditCardAccountUpdated(String creditCardAccountUpdated) {
        this.creditCardAccountUpdated = creditCardAccountUpdated;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Date getReadDttm() {
        return readDttm;
    }

    public void setReadDttm(Date readDttm) {
        this.readDttm = readDttm;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public Date getCreatedDttm() {
        return createdDttm;
    }

    public void setCreatedDttm(Date createdDttm) {
        this.createdDttm = createdDttm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestTransaction)) return false;
        RequestTransaction that = (RequestTransaction) o;
        return getReqOrdNum() == that.getReqOrdNum() &&
                getReqDate().equals(that.getReqDate()) &&
                getMerchantTransactionId().equals(that.getMerchantTransactionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReqDate(), getReqOrdNum(), getMerchantTransactionId());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RequestTransaction{");
        sb.append("reqDate='").append(reqDate).append('\'');
        sb.append(", reqOrdNum=").append(reqOrdNum);
        sb.append(", merchantTransactionId='").append(merchantTransactionId).append('\'');
        sb.append(", vid='").append(vid).append('\'');
        sb.append(", timestamp='").append(timestamp).append('\'');
        sb.append(", amount='").append(amount).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", divisionNumber='").append(divisionNumber).append('\'');
        sb.append(", selectTransactionId='").append(selectTransactionId).append('\'');
        sb.append(", selectRefundId='").append(selectRefundId).append('\'');
        sb.append(", subscriptionId='").append(subscriptionId).append('\'');
        sb.append(", subscriptionStartDate='").append(subscriptionStartDate).append('\'');
        sb.append(", billingFrequency='").append(billingFrequency).append('\'');
        sb.append(", previousBillingDate='").append(previousBillingDate).append('\'');
        sb.append(", previousBillingCount='").append(previousBillingCount).append('\'');
        sb.append(", customerId='").append(customerId).append('\'');
        sb.append(", paymentMethodId='").append(paymentMethodId).append('\'');
        sb.append(", paymentMethodIsTokenized='").append(paymentMethodIsTokenized).append('\'');
        sb.append(", creditCardAccount='").append(creditCardAccount).append('\'');
        sb.append(", creditCardAccountHash='").append(creditCardAccountHash).append('\'');
        sb.append(", creditCardExpirationDate='").append(creditCardExpirationDate).append('\'');
        sb.append(", accountHolderName='").append(accountHolderName).append('\'');
        sb.append(", billingAddressLine1='").append(billingAddressLine1).append('\'');
        sb.append(", billingAddressLine2='").append(billingAddressLine2).append('\'');
        sb.append(", billingAddressLine3='").append(billingAddressLine3).append('\'');
        sb.append(", billingAddressCity='").append(billingAddressCity).append('\'');
        sb.append(", billingAddressCounty='").append(billingAddressCounty).append('\'');
        sb.append(", billingAddressDistrict='").append(billingAddressDistrict).append('\'');
        sb.append(", billingAddressPostalcode='").append(billingAddressPostalcode).append('\'');
        sb.append(", billingAddressCountry='").append(billingAddressCountry).append('\'');
        sb.append(", nameValues='").append(nameValues).append('\'');
        sb.append(", affiliateId='").append(affiliateId).append('\'');
        sb.append(", affiliateSubId='").append(affiliateSubId).append('\'');
        sb.append(", billingStatementIdentifier='").append(billingStatementIdentifier).append('\'');
        sb.append(", authCode='").append(authCode).append('\'');
        sb.append(", avsCode='").append(avsCode).append('\'');
        sb.append(", cvnCode='").append(cvnCode).append('\'');
        sb.append(", creditCardAccountUpdated='").append(creditCardAccountUpdated).append('\'');
        sb.append(", isRead='").append(isRead).append('\'');
        sb.append(", readDttm=").append(readDttm);
        sb.append(", pageNum=").append(pageNum);
        sb.append(", returnCode='").append(returnCode).append('\'');
        sb.append(", returnMsg='").append(returnMsg).append('\'');
        sb.append(", createdDttm=").append(createdDttm);
        sb.append('}');
        return sb.toString();
    }
}
