package com.Budgethanteraren;

import java.time.LocalDate;

public record Transaction(LocalDate date, String category, double amount, TransactionType type) {}

