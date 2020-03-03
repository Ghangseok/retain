package com.att.retain.bill.repository;

import com.att.retain.bill.model.RequestCommunication;
import com.att.retain.bill.model.RequestCommunicationId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestCommunicationRepository extends CrudRepository<RequestCommunication, RequestCommunicationId> {

}
