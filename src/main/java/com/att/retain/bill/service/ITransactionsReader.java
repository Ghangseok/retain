package com.att.retain.bill.service;

import com.vindicia.soap.v1_1.selecttypes.Transaction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface ITransactionsReader {
    Pair<Integer, List<Transaction>> getTransaction(int page, int pageSize);
}
