package com.att.retain.bill.service;

import com.att.retain.bill.model.RequestCommunication;
import com.att.retain.bill.model.RequestCommunicationId;
import com.att.retain.bill.model.RequestTransaction;
import com.att.retain.bill.model.RequestTransactionId;
import com.att.retain.bill.repository.RequestCommunicationRepository;
import com.att.retain.bill.repository.RequestTransactionRepository;
import com.vindicia.soap.v1_1.select.BillTransactionsResponse;
import com.vindicia.soap.v1_1.selecttypes.Return;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import com.vindicia.soap.v1_1.selecttypes.TransactionValidationResponse;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.att.retain.bill.util.BillUtils.getNow;

public class ResponseDatabaseWriter implements IResponseWriter {
    static Logger log = LoggerFactory.getLogger(ResponseDatabaseWriter.class);

    @Autowired
    RequestTransactionRepository requestTransactionRepository;

    @Autowired
    RequestCommunicationRepository requestCommunicationRepository;

    @Override
    public Pair<Integer, Integer> writeSubmitResult(RequestCommunicationId reqCommId, List<Transaction> transactions, BillTransactionsResponse billTransactionsResponse) {
        int nSubmittedTransactions = 0, nSubmissionFailures = 0;
        log.debug("RequestCommunicationId : " + reqCommId.toString());

        if (billTransactionsResponse != null) {
            Return btxsReturn = billTransactionsResponse.get_return();  // The result of SOAP request
            String soapId = btxsReturn.getSoapId();
            int returnCode = btxsReturn.getReturnCode().getValue();
            String returnMsg = btxsReturn.getReturnString();

            RequestCommunication requestCommunication = new RequestCommunication();
            requestCommunication.setReqDate(reqCommId.getReqDate());
            requestCommunication.setReqOrdNum(reqCommId.getReqOrdNum());
            requestCommunication.setPageNum(reqCommId.getPageNum());
            requestCommunication.setSoapId(soapId);
            requestCommunication.setStatus(String.valueOf(returnCode));
            requestCommunication.setMsg(returnMsg);
            requestCommunication.setSubmitDttm(getNow("yyyy-MM-dd'T'HH:mm:ss Z"));
            requestCommunication.setCreatedDttm(getNow("yyyy-MM-dd'T'HH:mm:ss Z"));
            requestCommunicationRepository.save(requestCommunication);

            if (returnCode == 200) {
                TransactionValidationResponse[] response = billTransactionsResponse.getResponse(); // the result of each transactions.
                if (response != null) {
                    List<RequestTransaction> listRequestTransaction = new ArrayList<>();
                    for (TransactionValidationResponse txValidResponse : response) {
                        if (txValidResponse != null) {
                            nSubmissionFailures++;

                            RequestTransactionId requestTransactionId = new RequestTransactionId();
                            requestTransactionId.setReqDate(reqCommId.getReqDate());
                            requestTransactionId.setReqOrdNum(reqCommId.getReqOrdNum());
                            requestTransactionId.setMerchantTransactionId(txValidResponse.getMerchantTransactionId());

                            RequestTransaction reqTx = requestTransactionRepository.findById(requestTransactionId).get();
                            reqTx.setPageNum(reqCommId.getPageNum());
                            reqTx.setReturnCode(String.valueOf(txValidResponse.getCode()));
                            reqTx.setReturnMsg(txValidResponse.getDescription());
                            listRequestTransaction.add(reqTx);
                        }
                        requestTransactionRepository.saveAll(listRequestTransaction);
                    }
                } else { // this soap call was successful.
                    List<RequestTransaction> listRequestTransaction = new ArrayList<>();
                    for (Transaction transaction : transactions) {
                        nSubmittedTransactions++;
                        RequestTransactionId requestTransactionId = new RequestTransactionId();
                        requestTransactionId.setReqDate(reqCommId.getReqDate());
                        requestTransactionId.setReqOrdNum(reqCommId.getReqOrdNum());
                        requestTransactionId.setMerchantTransactionId(transaction.getMerchantTransactionId());

                        RequestTransaction reqTx = requestTransactionRepository.findById(requestTransactionId).get();
                        reqTx.setPageNum(reqCommId.getPageNum());
                        reqTx.setReturnCode(String.valueOf(returnCode));
                        reqTx.setReturnMsg(returnMsg);
                        listRequestTransaction.add(reqTx);
                    }
                    requestTransactionRepository.saveAll(listRequestTransaction);
                }
            } else {
                nSubmissionFailures += transactions.size();
            }
        } else {
            nSubmissionFailures += transactions.size();

            RequestCommunication requestCommunication = new RequestCommunication();
            requestCommunication.setReqDate(reqCommId.getReqDate());
            requestCommunication.setReqOrdNum(reqCommId.getReqOrdNum());
            requestCommunication.setPageNum(reqCommId.getPageNum());
            requestCommunication.setStatus("503");
            requestCommunication.setMsg("Communication Failed (Soap Server)!");
            requestCommunication.setSubmitDttm(getNow("yyyy-MM-dd'T'HH:mm:ss Z"));
            requestCommunication.setCreatedDttm(getNow("yyyy-MM-dd'T'HH:mm:ss Z"));
            requestCommunicationRepository.save(requestCommunication);

            log.error("\n\tPage " + reqCommId.getPageNum() + " Transactions : [503] Communication Failed (Soap Server)");
        }
        return new ImmutablePair<>(nSubmittedTransactions, nSubmissionFailures);
    }
}
