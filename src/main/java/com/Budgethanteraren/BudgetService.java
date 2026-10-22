package com.Budgethanteraren;

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

}
