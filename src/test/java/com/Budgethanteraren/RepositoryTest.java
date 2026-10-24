package com.Budgethanteraren;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    @Test
    void addShouldAddItem() {
        //Arrange
        Repository<String> repository = new Repository<>();
        //Act
        repository.add("Hej");
        //Assert
        assertEquals(1, repository.findAll().size());
        assertEquals("Hej", repository.findAll().getFirst());
    }

    @Test
    void findAllShouldReturnEmptyListWhenRepositoryIsEmpty() {
        //Arrange
        Repository<String> repository = new Repository<>();
        //Act
        List<String> result = repository.findAll();
        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnAllItems() {
        //Arrange
        Repository<String> repository = new Repository<>();
        repository.add("Ett");
        repository.add("Två");
        repository.add("Tre");
        //Act
        List<String> result = repository.findAll();
        //Assert
        assertEquals(3, result.size());
        assertEquals("Ett", result.get(0));
        assertEquals("Två", result.get(1));
        assertEquals("Tre", result.get(2));
    }

    @Test
    void findWhereShouldFilterItems() {
        //Arrange
        Repository<Integer> repository = new Repository<>();
        repository.add(1);
        repository.add(2);
        repository.add(3);
        //Act
        List<Integer> result = repository.findWhere(x -> x > 1);
        //Assert
        assertEquals(2, result.size());
        assertEquals(2, result.get(0));
        assertEquals(3, result.get(1));
    }

    @Test
    void findWhereShouldReturnEmptyListWhenNothingMatchesPredicate() {
        //Arrange
        Repository<Integer> repository = new Repository<>();
        repository.add(1);
        repository.add(2);
        repository.add(3);
        //Act
        List<Integer> result = repository.findWhere(x -> x > 5);
        //Assert
        assertTrue(result.isEmpty());
    }

}