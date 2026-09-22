package ru.lakeevda.unit.application.mapper;

import org.junit.jupiter.api.Test;
import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.application.mapper.OrderUseCaseMapper;
import ru.lakeevda.domain.model.order.Order;
import ru.lakeevda.domain.model.order.OrderId;
import ru.lakeevda.domain.model.order.OrderName;
import ru.lakeevda.domain.model.order.OrderStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderUseCaseMapperTest {
    @Test
    public void testToDomain() {
        var req = new OrderParamRequest("Test", "CREATED");
        Order order = OrderUseCaseMapper.toDomain(req);
        assertEquals("Test", order.getName().getValue());
        assertEquals("CREATED", order.getStatus().getValue());
    }

    @Test
    public void testFromDomain() {
        Order order = Order.restore(OrderId.of(1L), OrderName.of("Demo"), OrderStatus.COMPLETED);
        var resp = OrderUseCaseMapper.fromDomain(order);
        assertEquals(1L, resp.id());
        assertEquals("Demo", resp.name());
        assertEquals("COMPLETED", resp.status());
    }
}
