package com.poc.model;

public class Category {

    private final String category;

    public Category(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category='" + category + '\'' +
                '}';
    }

}
