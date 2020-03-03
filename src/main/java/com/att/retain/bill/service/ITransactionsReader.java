package com.att.retain.bill.service;

import com.att.retain.bill.model.RequestCommunicationId;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface ITransactionsReader {
    Pair<RequestCommunicationId, List<Transaction>> getTransaction(int page, int pageSize);
}
