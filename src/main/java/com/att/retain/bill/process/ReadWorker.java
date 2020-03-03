package com.att.retain.bill.process;

import com.att.retain.bill.model.RequestCommunicationId;
import com.att.retain.bill.service.ITransactionsReader;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.concurrent.Callable;

public class ReadWorker implements Callable<Pair<RequestCommunicationId, List<Transaction>>> {
    private ITransactionsReader reader;
    private Integer pageNum;
    private Integer pageSize;

    public ReadWorker(ITransactionsReader transactionsReader, int pageNum, int pageSize) {

        this.reader = transactionsReader;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    public Pair<RequestCommunicationId, List<Transaction>> call() throws Exception {
        return reader.getTransaction(pageNum, pageSize);
    }
}
