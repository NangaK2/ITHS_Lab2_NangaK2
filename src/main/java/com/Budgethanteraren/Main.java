package com.Budgethanteraren;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Repository<Transaction> repository = new Repository<>();
    private static final BudgetService budgetService = new BudgetService(repository);
    private static final FileService fileService = new FileService("transactions.csv");

    static void main() {
        logger.setLevel(Level.WARNING);
        Scanner scanner = new Scanner(System.in);

        try {
            List <Transaction> savedTransactions = fileService.load();
            if (savedTransactions.isEmpty()) {
                IO.println("There are no transactions saved.");
                logger.info("There are no transactions saved.");
            } else {
                repository.addAll(savedTransactions);
                IO.println("Transactions loaded from file.");
                logger.info("Transactions loaded from file.");
            }
        } catch (Exception e) {
            IO.println("Failed to load transactions from file.");
            logger.severe("Failed to load transactions from file." + e.getMessage());
        }

        boolean running = true;
        int choice;

        do {
            IO.println();
            IO.println("Budgethanteraren");
            IO.println("1. Add transaction");
            IO.println("2. Show all transactions");
            IO.println("3. Show balance and summary by category");
            IO.println("4. Filter transactions");
            IO.println("5. Save to file");
            IO.println("6. Exit");

            // Read user input safely and handle invalid input
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left-over
            } else {
                IO.println("Invalid input. Please enter a number");
                scanner.nextLine(); // Clear the invalid input
                continue;
            }


            switch (choice) {
                case 1 -> {
                    IO.println("Category:");
                    String category = scanner.nextLine().trim();
                    if (category.trim().isEmpty()) {
                        IO.println("Category cannot be empty");
                        logger.warning("No category was entered");
                        continue;
                    }

                    IO.println("Amount:");
                    double amount;
                    if (scanner.hasNextDouble()) {
                        amount = scanner.nextDouble();
                    }
                    else {
                        IO.println("Invalid amount, enter a valid number!");
                        logger.warning("Invalid amount entered");
                        scanner.nextLine();
                        continue;
                    }

                    IO.println("INCOME or EXPENSE:");
                    scanner.nextLine();
                    TransactionType type;
                    String input = scanner.nextLine();
                    if(input.trim().equalsIgnoreCase("INCOME")) {
                        type = TransactionType.INCOME;
                        logger.info("Transaction type set to INCOME");
                    }
                    else if(input.trim().equalsIgnoreCase("EXPENSE")) {
                        type = TransactionType.EXPENSE;
                        logger.info("Transaction type set to EXPENSE");
                    }
                    else {
                        IO.println("Invalid choice, enter INCOME or EXPENSE!");
                        logger.warning("Invalid transaction type entered");
                        scanner.nextLine();
                        continue;
                    }

                    try {
                        Transaction transaction = new Transaction(LocalDate.now(), category, amount, type);
                        budgetService.addTransaction(transaction);
                        IO.println("Transaction added");
                        logger.info("Transaction added: " + transaction);
                    } catch (InvalidTransactionException e) {
                        IO.println("Could not add transaction");
                        logger.warning("Could not add transaction: " + e.getMessage());
                    }
                }

                case 2 -> {
                    List <Transaction> allTransactions = repository.findAll();
                    if (allTransactions.isEmpty()) {
                        IO.println("No transactions registered");
                    }
                    else {
                        allTransactions.forEach(IO::println);
                        logger.info("Transactions retrieved from repository");
                    }
                }

                case 3 -> {
                        IO.println("Balance: " + budgetService.getBalance());
                        logger.info("Balance retrieved");
                        Map<String, Double> mapWithCategorySum = budgetService.getCategorySum();
                        if(mapWithCategorySum.isEmpty()) {
                            IO.println("No transactions registered");
                        }
                        else {
                            IO.println(mapWithCategorySum);
                            logger.info("Category sums retrieved from repository");
                        }
                }

                case 4 -> filterTransactions(scanner); //Opening sub-menu

                case 5 -> {
                    IO.println("Saving to file...");
                    List<Transaction> allTransactionsToSave = repository.findAll();
                    if (allTransactionsToSave.isEmpty()) {
                        IO.println("There are no transactions to save");
                        logger.info("There are no transactions to save");
                    }
                    else {
                        logger.info("Transactions retrieved from repository");

                        try {
                            fileService.save(allTransactionsToSave);
                            IO.println("Transactions saved to file.");
                            logger.info("Transactions saved to file.");
                        } catch (IOException e) {
                            IO.println("Failed to save file.");
                            logger.severe("Failed to save file. " + e.getMessage());
                        }
                    }
                }

                case 6 -> {
                    running = false;
                    IO.println("Closing application...goodbye!");
                }

                default -> IO.println("Invalid choice, please enter a number between 1 and 6");
            }
        } while (running);
    }

    private static void filterTransactions(Scanner scanner) {
        IO.println("""
                ----- Sub-menu -----
                How do you want to filter transactions?
                1. Show transactions of type INCOME.
                2. Show transactions of type EXPENSE.
                3. Show transactions by date.
                4. Show transactions by category.
                5. Back to main menu.
                """);

        String input = scanner.nextLine();

        switch (input) {
            case "1":
                List<Transaction> transactionsByTypeIncome = budgetService.getByType(TransactionType.INCOME);
                if(transactionsByTypeIncome.isEmpty()) {
                    IO.println("No transactions registered of type INCOME");
                } else {
                    transactionsByTypeIncome.forEach(IO::println);
                    logger.info("Transactions retrieved by type INCOME");
                }
                break;

            case "2":
                List<Transaction> transactionsByTypeExpense = budgetService.getByType(TransactionType.EXPENSE);
                if (transactionsByTypeExpense.isEmpty()) {
                    IO.println("No transactions registered of type EXPENSE");
                } else {
                    transactionsByTypeExpense.forEach(IO::println);
                    logger.info("Transactions retrieved by type EXPENSE");
                }
                break;

            case "3":
                LocalDate date;
                while (true) {
                    try {
                        IO.println("Enter a date (YYYY-MM-DD): ");
                        String dateInput = scanner.nextLine();
                        date = LocalDate.parse(dateInput);
                        if (date.isAfter(LocalDate.now())) {
                            IO.println("Date has not yet occurred. Enter today's date or an earlier date.");
                            logger.info("Date has not yet occurred. Enter today's date or an earlier date.");
                            continue;
                        }
                        break;
                    } catch (DateTimeParseException e) {
                        IO.println("Invalid date format: " + e.getMessage());
                        logger.warning("Invalid date format: " +  e.getMessage());
                        //scanner.nextLine();
                    }
                }

                List<Transaction> transactionsByDate = budgetService.getByDate(date);
                if (transactionsByDate.isEmpty()) {
                    IO.println("No transactions registered on this date: " + date);
                } else {
                    transactionsByDate.forEach(IO::println);
                    logger.info("Transactions retrieved by date");
                }
                break;

            case "4":
                IO.println("Enter a category: ");
                String category = scanner.nextLine().trim();
                List<Transaction> transactionsByCategory = budgetService.getByCategory(category);
                if (transactionsByCategory.isEmpty()) {
                    IO.println("No transactions registered in this category: " + category);
                } else {
                    transactionsByCategory.forEach(IO::println);
                    logger.info("Transactions retrieved by category");
                }
                break;

            case "5":
                break;

            default:
                IO.println("Invalid choice, please enter a number between 1 and 5!");
        }
    }

}