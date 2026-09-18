package ru.lakeevda.domain.model.order;

public class OrderName {
    private final String value;

    private OrderName(String value) {
        this.value = value;
    }

    public static OrderName of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Название не может быть null или пустым");
        }
        return new OrderName(value.trim());
    }

    public String getValue() {
        return value;
    }
}
