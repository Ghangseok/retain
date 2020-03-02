package com.att.retain.bill.config;

import com.att.retain.bill.process.ReadWorker;
import com.att.retain.bill.service.ITransactionsReader;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SEL001BillSelectConfig {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ReadWorker createReadWorker(ITransactionsReader transactionsReader, int pageNum, int pageSize) {
        return new ReadWorker(transactionsReader, pageNum, pageSize);
    }
}
