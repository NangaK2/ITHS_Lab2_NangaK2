package com.Budgethanteraren;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetService {

    private final Repository<Transaction> repository;

    public BudgetService(Repository<Transaction> repository) {
        this.repository = repository;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction.amount() <= 0) {
            throw new InvalidTransactionException("Amount must be larger than 0");
        }
        repository.add(transaction);
    }

    public double getBalance() {
        return repository.findAll()
                .stream()
                .mapToDouble(transaction ->
                        transaction.type() == TransactionType.INCOME
                        ? transaction.amount()
                        : -transaction.amount())
                .sum();
    }

    public Map<String, Double> getCategorySummary() {
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Transaction::category,
                        Collectors.summingDouble(transaction ->
                                transaction.type() == TransactionType.INCOME
                                        ? transaction.amount()
                                        : -transaction.amount())
                ));
    }

    public List<Transaction> getByType(TransactionType type) {
        return repository.findAll()
                .stream()
                .filter(transaction ->
                        transaction.type() == type)
                .toList();
    }

}
