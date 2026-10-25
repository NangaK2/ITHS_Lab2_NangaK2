package com.Budgethanteraren;

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

    static void main() {
        logger.setLevel(Level.INFO);
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        int choice;

        do {
            IO.println();
            IO.println("Budgethanteraren");
            IO.println("1. Add transaction");
            IO.println("2. Show all transactions");
            IO.println("3. Show balance and summary by category");
            IO.println("4. Filter transaktions");
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
                    String category = scanner.nextLine();

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
                        logger.info("Transaction added: " + transaction);
                    } catch (InvalidTransactionException e) {
                        IO.println("Could not add transaction");
                        logger.warning("Could not add transaction: " + e.getMessage());
                    }
                }

                case 2 -> repository.findAll()
                        .forEach(IO::println);

                case 3 -> {
                        IO.println("Balance: " + budgetService.getBalance());
                        Map<String, Double> mapWithCategorySum = budgetService.getCategorySum();
                        IO.println(mapWithCategorySum);
                }

                case 4 -> filterTransactions(scanner);

                case 5 -> {
                    IO.println("Saving to file...");
                    //Last thing to implement
                    //budgetService.saveToFile();
                    IO.println("Saved to file!");
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
                How do you want to filter transactions?
                1. Show transactions of type INCOME.
                2. Show transactions of type EXPENSE.
                3. Show transactions by date.
                4. Back to main menu.
                """);

        String input = scanner.nextLine();

        switch (input) {
            case "1":
                budgetService.getByType(TransactionType.INCOME)
                        .forEach(IO::println);
                break;

            case "2":
                budgetService.getByType(TransactionType.EXPENSE)
                        .forEach(IO::println);
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
                            continue;
                        }
                        break;
                    } catch (DateTimeParseException e) {
                        IO.println("Invalid date format: " + e.getMessage());
                        logger.warning("Invalid date format: " +  e.getMessage());
                        //scanner.nextLine();
                    }
                }

                List<Transaction> transactions = budgetService.getByDate(date);
                if (transactions.isEmpty()) {
                    IO.println("No transactions registered on this date");
                } else {
                    transactions.forEach(IO::println);
                }
                break;

            case "4":
                break;

            default:
                IO.println("Invalid choice, please enter a number between 1 and 4!");
        }
    }

}