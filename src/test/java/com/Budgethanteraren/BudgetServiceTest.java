package com.Budgethanteraren;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BudgetServiceTest {

    @Test
    void getBalanceShouldAddIncomeAndSubtractExpense() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        repository.add(new Transaction(LocalDate.now(), "Salary", 1000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Food", 300, TransactionType.EXPENSE));
        //Act
        double result = budgetService.getBalance();
        //Assert
        assertEquals(700, result);
    }

    @Test
    void getBalanceShouldReturnNegativeAmountWhenExpenseIsHigherThanIncome() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        repository.add(new Transaction(LocalDate.now(), "Salary", 1000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Food", 1500, TransactionType.EXPENSE));
        //Act
        double result = budgetService.getBalance();
        //Assert
        assertEquals(-500, result);
    }

    @Test
    void getBalanceShouldReturnZeroWhenNoTransactions() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        //Act
        double result = budgetService.getBalance();
        //Assert
        assertEquals(0, result);
    }

    @Test
    void getBalanceShouldReturnZeroWhenIncomeEqualsExpense() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        repository.add(new Transaction(LocalDate.now(), "Salary", 1000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Food", 1000, TransactionType.EXPENSE));
        //Act
        double result = budgetService.getBalance();
        //Assert
        assertEquals(0, result);
    }

    @Test
    void getCategorySumShouldReturnEmptyMapWhenRepositoryIsEmpty() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        //Act
        Map<String, Double> result = budgetService.getCategorySum();
        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getCategorySumShouldSumItemsFromTheSameCategory() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(LocalDate.now(), "Salary", 15000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Salary", 15000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Food", 200, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Food", 500, TransactionType.EXPENSE));
        BudgetService budgetService = new BudgetService(repository);
        //Act
        Map<String, Double> result = budgetService.getCategorySum();
        //Assert
        assertEquals(30000, result.get("salary"));
    }

}

