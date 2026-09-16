package ru.lakeevda.domain.entity;

public class Order {
    private Long id;
    private final String name;
    private final String status;

    private Order(String name, String status) {
        this.name = name;
        this.status = status;
    }

    private Order(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static Order create(String name, String status) {
        return new Order(name, status);
    }

    public static Order restore(Long id, String name, String status) {
        return new Order(id, name, status);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}