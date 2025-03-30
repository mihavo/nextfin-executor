package com.nextfin.account.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WithdrawDto(@Positive BigDecimal amount) {
}
