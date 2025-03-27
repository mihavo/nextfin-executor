package com.nextfin.executor.service.impl;

import com.nextfin.executor.service.ExecutorService;
import com.nextfin.transaction.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ExecutorServiceImpl implements ExecutorService {
    @Override
    public Transaction process(Transaction transaction) {
        return null;
    }
}
