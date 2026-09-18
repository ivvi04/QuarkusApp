package ru.lakeevda.infrastructure.persistence.mapper;

import ru.lakeevda.domain.model.order.Order;
import ru.lakeevda.domain.model.order.OrderId;
import ru.lakeevda.domain.model.order.OrderName;
import ru.lakeevda.domain.model.order.OrderStatus;
import ru.lakeevda.infrastructure.persistence.entity.OrderEntity;

public class OrderRepositoryMapper {
    public static OrderEntity toEntity(Order domain) {
        var entity = new OrderEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }
        entity.setName(domain.getName().getValue());
        entity.setStatus(domain.getStatus().getValue());

        return entity;
    }

    public static Order fromEntity(OrderEntity entity) {
        return Order.restore(
                OrderId.of(entity.getId()),
                OrderName.of(entity.getName()),
                OrderStatus.fromValue(entity.getStatus()));
    }
}
