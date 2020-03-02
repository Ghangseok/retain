package com.att.retain.bill.service;

import com.vindicia.soap.v1_1.select.BillTransactionsResponse;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IResponseWriter {
    Pair<Integer, Integer> writeSubmitResult(final int pageNum, List<Transaction> transactions, final BillTransactionsResponse billTransactionsResponse);
}
