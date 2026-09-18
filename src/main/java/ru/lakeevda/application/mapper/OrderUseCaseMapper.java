package ru.lakeevda.application.mapper;

import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.application.dto.OrderParamResponse;
import ru.lakeevda.domain.model.order.Order;
import ru.lakeevda.domain.model.order.OrderName;
import ru.lakeevda.domain.model.order.OrderStatus;

public class OrderUseCaseMapper {
    public static Order toDomain(OrderParamRequest param) {
        return Order.create(
                OrderName.of(param.name()),
                OrderStatus.fromValue(param.status()));
    }

    public static OrderParamResponse fromDomain(Order order) {
        return new OrderParamResponse(
                order.getId().getValue(),
                order.getName().getValue(),
                order.getStatus().getValue());
    }
}
