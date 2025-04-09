package com.nextfin.executor.listeners;

import com.nextfin.executor.service.ExecutorService;
import com.nextfin.transaction.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionListener {

    private final ExecutorService executorService;

    @KafkaListener(topics = "transactions")
    public void listen(Transaction transaction) {
        log.debug("Received transaction " + transaction.getId() + " for processing.");
        executorService.process(transaction);
    }
}
