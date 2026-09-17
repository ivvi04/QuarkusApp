package ru.lakeevda.application.repository.order;

import ru.lakeevda.domain.entity.order.Order;
import ru.lakeevda.domain.entity.order.OrderId;
import ru.lakeevda.domain.entity.order.OrderName;
import ru.lakeevda.domain.entity.order.OrderStatus;
import ru.lakeevda.infrastructure.entity.OrderEntity;

public class OrderRepositoryMapper {

    static OrderEntity toEntity(Order domain) {
        var entity = new OrderEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }
        entity.setName(domain.getName().getValue());
        entity.setStatus(domain.getStatus().getValue());

        return entity;
    }

    static Order fromEntity(OrderEntity entity) {
        return Order.restore(
                OrderId.of(entity.getId()),
                OrderName.of(entity.getName()),
                OrderStatus.fromValue(entity.getStatus()));
    }
}
