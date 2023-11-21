package br.com.pedromonteiro.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    
    private PlatformTransactionManager transactionManager;

    public BatchConfig(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    
}