package com.Budgethanteraren;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
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

    @Test
    void getCategoryTotalsShouldHandleMultipleCategories() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        repository.add(new Transaction(LocalDate.now(), "Salary", 15000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Food", 500, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Snusstock", 300, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Rent", 6000, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "food", 300, TransactionType.EXPENSE));
        BudgetService budgetService = new BudgetService(repository);
        //Act
        Map<String, Double> result = budgetService.getCategorySum();
        //Assert
        assertEquals(15000, result.get("salary"));
        assertEquals(-800, result.get("food"));
        assertEquals(4, result.size());
    }

    @Test
    void getByTypeShouldReturnOnlyIncomeTransactions() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        repository.add(new Transaction(LocalDate.now(), "Salary", 15000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Food", 500, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Snusstock", 300, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Rent", 6000, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "food", 300, TransactionType.EXPENSE));
        //Act
        List<Transaction> result = budgetService.getByType(TransactionType.INCOME);
        //Assert
        assertEquals(1, result.size());
        assertEquals(TransactionType.INCOME, result.getFirst().type());
    }

    @Test
    void getByTypeShouldReturnOnlyExpenseTransactions() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        repository.add(new Transaction(LocalDate.now(), "Salary", 15000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Food", 500, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Snusstock", 300, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Rent", 6000, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "food", 300, TransactionType.EXPENSE));
        //Act
        List<Transaction> result = budgetService.getByType(TransactionType.EXPENSE);
        //Assert
        assertEquals(4, result.size());
        assertEquals(TransactionType.EXPENSE, result.getFirst().type());
    }

    @Test
    void getByDateShouldReturnOnlyTransactionsForGivenDate() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        repository.add(new Transaction(LocalDate.of(2026, 10, 20), "Salary", 15000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.of(2026, 10, 20), "Food", 500, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.of(2026, 10, 20), "Snusstock", 300, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Rent", 6000, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "food", 300, TransactionType.EXPENSE));
        //Act
        List<Transaction> result = budgetService.getByDate(LocalDate.of(2026, 10, 20));
        //Assert
        assertEquals(3, result.size());
        assertEquals("Salary", result.getFirst().category());
    }

    @Test
    void getByCategoryShouldReturnOnlyTransactionsForGivenCategory() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        repository.add(new Transaction(LocalDate.now(), "Salary", 15000, TransactionType.INCOME));
        repository.add(new Transaction(LocalDate.now(), "Food", 500, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Snusstock", 300, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "Rent", 6000, TransactionType.EXPENSE));
        repository.add(new Transaction(LocalDate.now(), "food", 300, TransactionType.EXPENSE));
        //Act
        List<Transaction> result = budgetService.getByCategory("salary");
        List<Transaction> result2 = budgetService.getByCategory("Food");
        //Assert
        assertEquals(1, result.size());
        assertEquals(2, result2.size());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        //Act & Assert
        assertThrows(InvalidTransactionException.class,() ->
                budgetService.addTransaction(new Transaction(LocalDate.now(), "Salary", 0, TransactionType.INCOME)));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        //Arrange
        Repository<Transaction> repository = new Repository<>();
        BudgetService budgetService = new BudgetService(repository);
        //Act & Assert
        assertThrows(InvalidTransactionException.class,() ->
                budgetService.addTransaction(new Transaction(LocalDate.now(), "Salary", -1000, TransactionType.INCOME)));
    }

}

