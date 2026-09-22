package ru.lakeevda.domain.model.order;

import java.util.Arrays;

public enum OrderStatus {
    CREATED("CREATED", "Создание"),
    EDITED("EDITED", "Редактирование"),
    COMPLETED("COMPLETED", "Выполнен");

    private final String value;
    private final String description;

    OrderStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderStatus fromValue(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Статус не может быть null или пустым");
        }

        return Arrays.stream(OrderStatus.values())
                .filter(candidate -> candidate.value.equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Статус с таким названием не существует"));
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
