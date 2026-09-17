package ru.lakeevda.domain.entity.order;

public class OrderId {
    private final Long value;

    private OrderId(Long value) {
        this.value = value;
    }

    public static OrderId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("Идентификатор не может быть null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Идентификатор должен быть положительным");
        }
        return new OrderId(value);
    }

    public Long getValue() {
        return value;
    }
}
