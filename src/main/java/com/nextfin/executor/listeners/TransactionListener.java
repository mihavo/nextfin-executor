package com.nextfin.executor.listeners;

import com.nextfin.executor.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionListener {

    @KafkaListener(topics = "transactions", id = "tnxListener")
    public void listen(Transaction transaction) {
        System.out.println(transaction);
    }
}
