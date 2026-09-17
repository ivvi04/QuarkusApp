package ru.lakeevda.application.usecase.order;

import ru.lakeevda.domain.boundary.model.order.OrderRequest;
import ru.lakeevda.domain.boundary.model.order.OrderResponse;
import ru.lakeevda.domain.entity.order.Order;
import ru.lakeevda.domain.entity.order.OrderName;
import ru.lakeevda.domain.entity.order.OrderStatus;

public class OrderUseCaseMapper {

    static Order toEntity(OrderRequest orderRequest) {
        return Order.create(
                OrderName.of(orderRequest.name()),
                OrderStatus.fromValue(orderRequest.status()));
    }

    static OrderResponse fromEntity(Order order) {
        return new OrderResponse(
                order.getId().getValue(),
                order.getName().getValue(),
                order.getStatus().getValue());
    }
}
