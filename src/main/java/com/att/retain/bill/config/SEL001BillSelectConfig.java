package com.att.retain.bill.config;

import com.att.retain.bill.process.ReadWorker;
import com.att.retain.bill.service.IResponseWriter;
import com.att.retain.bill.service.ITransactionsReader;
import com.att.retain.bill.service.ResponseDatabaseWriter;
import com.att.retain.bill.service.ResponseFileWriter;
import com.att.retain.bill.service.TransactionDatabaseReader;
import com.att.retain.bill.service.TransactionFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class SEL001BillSelectConfig {

//    @Value("${submit.billTransactions.maxThreadWorkers}")
//    Integer maxThreadWorkers;

    @Value("${submit.billTransactions.response_path}")
    String response_path;
    @Value("${submit.billTransactions.txSource}")
    String txSource;

    @Bean
    ITransactionsReader transactionsReader() {
        ITransactionsReader transactionsReader;
        if (txSource.equals("FILE")) {
            transactionsReader = new TransactionFileReader();
        } else {
            transactionsReader = new TransactionDatabaseReader();
        }
        return transactionsReader;
    }

    @Bean
    IResponseWriter responseWriter() {
        IResponseWriter responseWriter;
        if (txSource.equals("FILE")) {
            responseWriter = new ResponseFileWriter(response_path);
        } else {
            responseWriter = new ResponseDatabaseWriter();
        }
        return responseWriter;
    }
}
