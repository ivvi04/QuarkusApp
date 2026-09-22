package ru.lakeevda.unit.infrastructure.persistence.mapper;

import org.junit.jupiter.api.Test;
import ru.lakeevda.domain.model.order.Order;
import ru.lakeevda.domain.model.order.OrderId;
import ru.lakeevda.domain.model.order.OrderName;
import ru.lakeevda.domain.model.order.OrderStatus;
import ru.lakeevda.infrastructure.persistence.entity.OrderEntity;
import ru.lakeevda.infrastructure.persistence.mapper.OrderRepositoryMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderRepositoryMapperTest {
    @Test
    public void testToEntityWithId() {
        Order order = Order.restore(OrderId.of(42L), OrderName.of("TestOrder"), OrderStatus.CREATED);
        OrderEntity entity = OrderRepositoryMapper.toEntity(order);
        assertEquals(42L, entity.getId());
        assertEquals("TestOrder", entity.getName());
        assertEquals("CREATED", entity.getStatus());
    }

    @Test
    public void testToEntityWithoutId() {
        Order order = Order.create(OrderName.of("NoIdOrder"), OrderStatus.CREATED);
        OrderEntity entity = OrderRepositoryMapper.toEntity(order);
        assertNull(entity.getId());
        assertEquals("NoIdOrder", entity.getName());
        assertEquals("CREATED", entity.getStatus());
    }

    @Test
    public void testFromEntity() {
        OrderEntity entity = new OrderEntity();
        entity.setId(99L);
        entity.setName("EntityOrder");
        entity.setStatus("COMPLETED");
        Order order = OrderRepositoryMapper.fromEntity(entity);
        assertEquals(99L, order.getId().getValue());
        assertEquals("EntityOrder", order.getName().getValue());
        assertEquals("COMPLETED", order.getStatus().getValue());
    }
}
