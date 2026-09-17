package ru.lakeevda.domain.entity.order;

public class Order {
    private OrderId id;
    private final OrderName name;
    private final OrderStatus status;

    private Order(OrderName name, OrderStatus status) {
        this.name = name;
        this.status = status;
    }

    private Order(OrderId id, OrderName name, OrderStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static Order create(OrderName name, OrderStatus status) {
        return new Order(name, status);
    }

    public static Order restore(OrderId id, OrderName name, OrderStatus status) {
        return new Order(id, name, status);
    }

    public OrderId getId() {
        return id;
    }

    public OrderName getName() {
        return name;
    }

    public OrderStatus getStatus() {
        return status;
    }
}