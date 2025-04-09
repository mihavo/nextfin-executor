package com.nextfin.executor.service;

import com.nextfin.transaction.entity.Transaction;

public interface ExecutorService {
    /**
     * Executes the whole flow of the transaction: storing it to db, doing the transaction and updating the status
     *
     * @param transaction the transaction to be processed
     */
    void process(Transaction transaction);
}
