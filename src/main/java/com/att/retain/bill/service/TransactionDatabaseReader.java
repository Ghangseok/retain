package com.att.retain.bill.service;

import com.att.retain.bill.model.RequestCommunicationId;
import com.att.retain.bill.model.RequestTransaction;
import com.att.retain.bill.repository.RequestTransactionRepository;
import com.vindicia.soap.v1_1.selecttypes.BillingIntervalType;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import com.vindicia.soap.v1_1.selecttypes.TransactionStatusType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.att.retain.bill.util.BillUtils.getCalendar;
import static com.att.retain.bill.util.BillUtils.getNow;
import static com.att.retain.bill.util.BillUtils.getSystemInfo;

public class TransactionDatabaseReader implements ITransactionsReader {

    static Logger log = LoggerFactory.getLogger(TransactionDatabaseReader.class);

    @Autowired
    private RequestTransactionRepository requestTransactionRepository;

    @Override
    public Pair<RequestCommunicationId, List<Transaction>> getTransaction(int page, int pageSize) {
        RequestCommunicationId requestCommunicationId = new RequestCommunicationId();

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<RequestTransaction> txns = requestTransactionRepository.getRequestTransactions(pageable);

        List<Transaction> transactions = txns.stream().map(requestTransaction -> {
            Transaction tx = new Transaction();

            tx.setVID(requestTransaction.getVid());
            tx.setTimestamp(requestTransaction.getTimestamp() != null ?
                    getCalendar(requestTransaction.getTimestamp()) : null);
            tx.setAmount(requestTransaction.getAmount() != null ?
                    new BigDecimal(requestTransaction.getAmount()) : null);
            tx.setCurrency(requestTransaction.getCurrency());

            if (!TransactionStatusType.Failed.toString().equalsIgnoreCase(requestTransaction.getStatus()))
                log.info("Unexpected Status: " + requestTransaction.getStatus());
            TransactionStatusType txStatusType = TransactionStatusType.Failed;
            tx.setStatus(txStatusType);

            tx.setDivisionNumber(requestTransaction.getDivisionNumber());
            tx.setMerchantTransactionId(requestTransaction.getMerchantTransactionId());
            tx.setSelectTransactionId(requestTransaction.getSelectTransactionId());
            tx.setSelectRefundId(requestTransaction.getSelectRefundId());
            tx.setSubscriptionId(requestTransaction.getSubscriptionId());
            tx.setSubscriptionStartDate(requestTransaction.getSubscriptionStartDate() != null ?
                    getCalendar(requestTransaction.getSubscriptionStartDate()) : null);

            tx.setBillingFrequency(requestTransaction.getBillingFrequency() != null ?
                    BillingIntervalType.Factory.fromValue(requestTransaction.getBillingFrequency()) : BillingIntervalType.Factory.fromValue("Monthly"));

            tx.setPreviousBillingDate(requestTransaction.getPreviousBillingDate() != null ?
                    getCalendar(requestTransaction.getPreviousBillingDate()) : null);
            tx.setPreviousBillingCount(requestTransaction.getPreviousBillingCount() != null ?
                    Integer.parseInt(requestTransaction.getPreviousBillingCount()) : 0);
            tx.setCustomerId(requestTransaction.getCustomerId());
            tx.setPaymentMethodId(requestTransaction.getPaymentMethodId());

            tx.setPaymentMethodIsTokenized(requestTransaction.getPaymentMethodIsTokenized() != null ?
                    Boolean.valueOf(requestTransaction.getPaymentMethodIsTokenized()) : false);

            tx.setCreditCardAccount(requestTransaction.getCreditCardAccount());
            tx.setCreditCardAccountHash(requestTransaction.getCreditCardAccountHash());
            tx.setCreditCardExpirationDate(requestTransaction.getCreditCardExpirationDate());
            tx.setAccountHolderName(requestTransaction.getAccountHolderName());
            tx.setBillingAddressLine1(requestTransaction.getBillingAddressLine1());
            tx.setBillingAddressLine2(requestTransaction.getBillingAddressLine2());
            tx.setBillingAddressLine3(requestTransaction.getBillingAddressLine3());
            tx.setBillingAddressCity(requestTransaction.getBillingAddressCity());
            tx.setBillingAddressCounty(requestTransaction.getBillingAddressCounty());
            tx.setBillingAddressDistrict(requestTransaction.getBillingAddressDistrict());
            tx.setBillingAddressPostalCode(requestTransaction.getBillingAddressPostalcode());
            tx.setBillingAddressCountry(requestTransaction.getBillingAddressCountry());

            tx.setNameValues(getSystemInfo());

            tx.setAffiliateId(requestTransaction.getAffiliateId());
            tx.setAffiliateSubId(requestTransaction.getAffiliateSubId());
            tx.setBillingStatementIdentifier(requestTransaction.getBillingStatementIdentifier());
            tx.setAuthCode(requestTransaction.getAuthCode());
            tx.setAvsCode(requestTransaction.getAvsCode());
            tx.setCvnCode(requestTransaction.getCvnCode());
            tx.setCreditCardAccountUpdated(requestTransaction.getCreditCardAccountUpdated() != null ?
                    Boolean.valueOf(requestTransaction.getCreditCardAccountUpdated()) : false);

            requestTransaction.setIsRead("Y");
            requestTransaction.setReadDttm(getNow("yyyy-MM-dd'T'HH:mm:ss Z"));

            return tx;
        }).collect(Collectors.toList());
        if (txns.size() > 0) {
            requestTransactionRepository.saveAll(txns);
            requestCommunicationId.setReqDate(txns.get(0).getReqDate());
            requestCommunicationId.setReqOrdNum(txns.get(0).getReqOrdNum());
            requestCommunicationId.setPageNum(page);
        }

        return new ImmutablePair<>(requestCommunicationId, transactions);
    }

}
