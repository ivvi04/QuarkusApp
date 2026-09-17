package ru.lakeevda.application.boundary.adapter.producer;

import ru.lakeevda.domain.boundary.model.order.OrderResponse;

public interface OrderProducer {
    void publish(OrderResponse order);
}
