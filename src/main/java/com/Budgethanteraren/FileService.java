package com.Budgethanteraren;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileService {
    private final Path path;
    private static final Logger logger = Logger.getLogger(FileService.class.getName());

    public FileService(String fileName) {
        this.path = Path.of(fileName);
        logger.setLevel(Level.INFO);
        logger.info("FileService initialized with filename: " + fileName);
    }

    public void save(List<Transaction> transactions) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {   //Try with resources

            for (Transaction transaction : transactions) {

                writer.write(
                        transaction.date()
                                + ","
                                + transaction.category()
                                + ","
                                + transaction.amount()
                                + ","
                                + transaction.type()
                );

                writer.newLine();
            }
            logger.info("File saved successfully.");
        }
    }

    public List<Transaction> load() throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        //Check if file exists. If not, create an empty file and return an empty list
        if (!Files.exists(path)) {
            logger.info("File does not exist. Creating an empty file..");
            Files.createFile(path);
            logger.info("Empty file created successfully.");
            return transactions;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length != 4) {
                    continue;
                }

                //Improvements that could be made:
                //Implement FutureDateException here with e.g. name FutureDateException to handle future dates
                //Check if parts[1] is empty i.e no category -> ""
                //Check if parts[2] is 0 or negative i.e amount -> 0 or negative



                transactions.add(new Transaction(
                                LocalDate.parse(parts[0]),
                                parts[1],
                                Double.parseDouble(parts[2]),
                                TransactionType.valueOf(parts[3])
                        )
                );
            }
        }
        logger.info("File loaded successfully");
        return transactions;
    }

}
