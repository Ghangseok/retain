package com.att.retain.bill.service;

import com.vindicia.soap.v1_1.select.BillTransactionsResponse;
import com.vindicia.soap.v1_1.selecttypes.Return;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import com.vindicia.soap.v1_1.selecttypes.TransactionValidationResponse;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class ResponseDatabaseWriter implements IResponseWriter {

    @Override
    public Pair<Integer, Integer> writeSubmitResult(int pageNum, List<Transaction> transactions, BillTransactionsResponse billTransactionsResponse) {
        return null;
    }
}
