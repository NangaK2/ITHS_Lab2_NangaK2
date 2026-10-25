package com.Budgethanteraren;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Repository<T> {

    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public void addAll(List<T> newItems) {
        items.addAll(newItems);
    }

    public List<T> findAll() {
        return List.copyOf(items);
    }

    public List<T> findWhere(Predicate<T> predicate) {
        return items.stream()
                .filter(predicate)
                .toList();
    }

}
