package com.Budgethanteraren;

import java.time.LocalDate;
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

    //Method to calculate balance with Stream API
    public double getBalance() {
        return repository.findAll()
                .stream()
                .mapToDouble(transaction ->
                        transaction.type() == TransactionType.INCOME
                        ? transaction.amount()
                        : -transaction.amount())
                .sum();
    }

    //Calculate total-sum for each category.
    public Map<String, Double> getCategorySum() {
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.category().toLowerCase(),
                        Collectors.summingDouble(transaction ->
                                transaction.type() == TransactionType.INCOME
                                        ? transaction.amount()
                                        : -transaction.amount())
                ));
    }

    //This method retrieves transactions by their type using filter in method
    public List<Transaction> getByType(TransactionType type) {
        return repository.findAll()
                .stream()
                .filter(transaction ->
                        transaction.type() == type)
                .toList();
    }

    //This method retrieves transactions by date
    //but uses the generic findWhere method in the 'Repository class' instead,
    //whereby we utilize the lambda expression as the 'logical' predicate parameter.
    public List<Transaction> getByDate(LocalDate date) {
        return repository.findWhere(
                transaction -> transaction.date().equals(date)
        );
    }

    //This method retrieves transactions by category
    public List<Transaction> getByCategory(String category) {
        return repository.findWhere(
                transaction -> transaction.category().equalsIgnoreCase(category)
        );
    }

}
