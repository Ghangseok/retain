package com.att.retain.bill.service;

import com.att.retain.bill.model.RequestCommunicationId;
import com.att.retain.bill.util.BillUtils;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import com.vindicia.soap.v1_1.selecttypes.TransactionStatusType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import static com.att.retain.bill.util.BillUtils.find;
import static com.att.retain.bill.util.BillUtils.getNow;

public class TransactionFileReader implements ITransactionsReader {
    static Logger log = LoggerFactory.getLogger(TransactionFileReader.class);

    private int iHDR_timestamp;
    private int iHDR_amount;
    private int iHDR_currency;
    private int iHDR_status;
    private int iHDR_divisionNumber;
    private int iHDR_merchantTransactionId;
    private int iHDR_subscriptionId;
    private int iHDR_customerId;
    private int iHDR_paymentMethodId;
    private int iHDR_creditCardAccount;
    private int iHDR_creditCardExpirationDate;
    private int iHDR_billingAddressLine1;
    private int iHDR_billingAddressLine2;
    private int iHDR_billingAddressLine3;
    private int iHDR_billingAddressCity;
    private int iHDR_billingAddressDistrict;
    private int iHDR_billingAddressPostalCode;
    private int iHDR_billingAddressCountry;
    private int iHDR_authCode;
    private int iHDR_avsCode;
    private int iHDR_cvnCode;
    private int iHDR_paymentMethodIsTokenized;

    @Value("${submit.billTransactions.request_header}")
    String request_header;
    @Value("${submit.billTransactions.request_path}")
    String request_path;
    @Value("${submit.billTransactions.request_file}")
    String request_file;
    @Value("${submit.billTransactions.response_path}")
    private String[] hdrs;

    public TransactionFileReader() {
        super();
    }

    @Override
    public Pair<RequestCommunicationId, List<Transaction>> getTransaction(int page, int pageSize) {

        RequestCommunicationId requestCommunicationId = new RequestCommunicationId();
        requestCommunicationId.setReqDate(getNow("YYYYMMDD"));
        requestCommunicationId.setReqOrdNum(0);
        requestCommunicationId.setPageNum(page);

        List<Transaction> txns = new CopyOnWriteArrayList<>();

        String separator = System.getProperty("file.separator");
        String inputFile = request_path + separator + request_file;

        try (Stream<String> lines = Files.lines(Paths.get(inputFile))) {

            Iterator<String> iterator = lines.skip(pageSize*(page-1)+1).limit(pageSize).iterator();
            while (iterator.hasNext()){
                String[] request = iterator.next().split(",");
                txns.add(getTransaction(request));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return new ImmutablePair<>(requestCommunicationId, txns);
    }

    private Transaction getTransaction(String[] request) {
        hdrs = this.request_header.split(",");

        Transaction transaction = new Transaction();
        try {
            iHDR_timestamp = find(hdrs, "timestamp");
            iHDR_amount = find(hdrs, "amount");
            iHDR_currency = find(hdrs, "currency");
            iHDR_status = find(hdrs, "status");
            iHDR_divisionNumber = find(hdrs, "divisionNumber");
            iHDR_merchantTransactionId = find(hdrs, "merchantTransactionId");
            iHDR_subscriptionId = find(hdrs, "subscriptionId");
            iHDR_customerId = find(hdrs, "customerId");
            iHDR_paymentMethodId = find(hdrs, "paymentMethodId");
            iHDR_creditCardAccount = find(hdrs, "creditCardAccount");
            iHDR_creditCardExpirationDate = find(hdrs, "creditCardExpirationDate");
            iHDR_billingAddressLine1 = find(hdrs, "billingAddressLine1");
            iHDR_billingAddressLine2 = find(hdrs, "billingAddressLine2");
            iHDR_billingAddressLine3 = find(hdrs, "billingAddressLine3");
            iHDR_billingAddressCity = find(hdrs, "billingAddressCity");
            iHDR_billingAddressDistrict = find(hdrs, "billingAddressDistrict");
            iHDR_billingAddressPostalCode = find(hdrs, "billingAddressPostalCode");
            iHDR_billingAddressCountry = find(hdrs, "billingAddressCountry");
            iHDR_authCode = find(hdrs, "authCode");
            iHDR_avsCode = find(hdrs, "avsCode");
            iHDR_cvnCode = find(hdrs, "cvnCode");
            iHDR_paymentMethodIsTokenized = find(hdrs, "paymentMethodIsTokenized");

            String merchantTransactionId = request[iHDR_merchantTransactionId];
            String customerId = request[iHDR_customerId];
            String subscriptionId = request[iHDR_subscriptionId];
            String authCode = request[iHDR_authCode];
            String avsCode = (iHDR_avsCode < 0 ? "" : request[iHDR_avsCode]);
            String cvnCode = (iHDR_cvnCode < 0 ? "" : request[iHDR_cvnCode]);
            String creditCardAccount = request[iHDR_creditCardAccount];
            String amount = request[iHDR_amount];
            String paymentMethodId = request[iHDR_paymentMethodId];
            String billingAddressLine1 = request[iHDR_billingAddressLine1];
            String billingAddressLine2 = request[iHDR_billingAddressLine2];
            String billingAddressLine3 = request[iHDR_billingAddressLine3];
            String billingAddressCity = request[iHDR_billingAddressCity];
            String billingAddressDistrict = request[iHDR_billingAddressDistrict];
            String billingAddressCountry = request[iHDR_billingAddressCountry];
            String billingAddressPostalCode = request[iHDR_billingAddressPostalCode];
            String timestamp = request[iHDR_timestamp];
            String creditCardExpirationDate = request[iHDR_creditCardExpirationDate];
            String divisionNumber = request[iHDR_divisionNumber];
            String status = request[iHDR_status];
            String currency = request[iHDR_currency];
            String paymentMethodIsTokenized	= request[iHDR_paymentMethodIsTokenized];

            if (!Character.isDigit(amount.charAt(0)))
                amount = amount.substring(1);    // remove any currency symbol

            transaction = getTransaction(
                    merchantTransactionId,
                    customerId,
                    subscriptionId,
                    authCode,
                    avsCode,
                    cvnCode,
                    creditCardAccount,
                    new BigDecimal(amount),
                    paymentMethodId,
                    billingAddressLine1,
                    billingAddressLine2,
                    billingAddressLine3,
                    billingAddressCity,
                    billingAddressDistrict,
                    billingAddressCountry,
                    billingAddressPostalCode,
                    BillUtils.getCalendar(timestamp),
                    creditCardExpirationDate,
                    divisionNumber,
                    status,
                    currency,
                    paymentMethodIsTokenized.equals("0") ? false : true
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return transaction;
    }

    private  Transaction getTransaction(
            String merchantTransactionId,
            String customerId,
            String subscriptionId,
            String authCode,
            String avsCode,
            String cvnCode,
            String creditCardAccount,
            BigDecimal amount,
            String paymentMethodId,
            String billingAddressLine1,
            String billingAddressLine2,
            String billingAddressLine3,
            String billingAddressCity,
            String billingAddressDistrict,
            String billingAddressCountry,
            String billingAddressPostalCode,
            Calendar timestamp,
            String creditCardExpirationDate,
            String divisionNumber,
            String status,
            String currency,
            Boolean paymentMethodIsTokenized
    ) {

        Transaction trans = new Transaction();
        trans.setMerchantTransactionId(merchantTransactionId);

        trans.setTimestamp(timestamp);    // Calendar
        trans.setAmount(amount);            // BigDecimal

        trans.setCurrency(currency);

        if (!TransactionStatusType.Failed.toString().equalsIgnoreCase(status))
            log.error("Unexpected Status: " + status);

        TransactionStatusType tstStatus = TransactionStatusType.Failed;
        trans.setStatus(tstStatus);

        trans.setDivisionNumber(divisionNumber);
        trans.setSubscriptionId(subscriptionId);
        trans.setCustomerId(customerId);
        trans.setPaymentMethodId(paymentMethodId);

        trans.setPaymentMethodIsTokenized(paymentMethodIsTokenized);

        if (paymentMethodIsTokenized) {
            creditCardAccount = BillUtils.getBIN(creditCardAccount);
        }

        trans.setCreditCardAccount(creditCardAccount);
        trans.setCreditCardExpirationDate(creditCardExpirationDate);

        trans.setAuthCode(authCode);
        trans.setAvsCode(avsCode);
        trans.setCvnCode(cvnCode);

        trans.setBillingAddressLine1(billingAddressLine1);
        trans.setBillingAddressLine2(billingAddressLine2);
        trans.setBillingAddressLine3(billingAddressLine3);
        trans.setBillingAddressCity(billingAddressCity);
        trans.setBillingAddressDistrict(billingAddressDistrict);
        trans.setBillingAddressCountry(billingAddressCountry);

        trans.setBillingAddressPostalCode(billingAddressPostalCode);

        trans.setNameValues(BillUtils.getSystemInfo());

        return trans;
    }
}
