package com.Budgethanteraren;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {

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
}
