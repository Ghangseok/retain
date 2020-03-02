package com.att.retain.bill.repository;

import com.att.retain.bill.model.RequestTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestTransactionRepository extends PagingAndSortingRepository<RequestTransaction, String> {
    @Query("SELECT reqTx FROM RequestTransaction reqTx WHERE isRead = 'N'")
    List<RequestTransaction> getRequestTransactions(Pageable pageable);

}
