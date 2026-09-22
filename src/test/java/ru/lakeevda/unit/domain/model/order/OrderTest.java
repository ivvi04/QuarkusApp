package ru.lakeevda.unit.domain.model.order;

import org.junit.jupiter.api.Test;
import ru.lakeevda.domain.model.order.Order;
import ru.lakeevda.domain.model.order.OrderId;
import ru.lakeevda.domain.model.order.OrderName;
import ru.lakeevda.domain.model.order.OrderStatus;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    public void testCreateOrder() {
        // Given
        OrderName name = OrderName.of("Test Order");
        OrderStatus status = OrderStatus.CREATED;

        // When
        Order order = Order.create(name, status);

        // Then
        assertNotNull(order);
        assertNull(order.getId());
        assertEquals(name, order.getName());
        assertEquals(status, order.getStatus());
    }

    @Test
    public void testCreateOrderWithNullName() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Order.create(OrderName.of(null), OrderStatus.CREATED);
        });
    }

    @Test
    public void testCreateOrderWithNullStatus() {
        // Given
        OrderName name = OrderName.of("Test Order");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Order.create(name, OrderStatus.fromValue(null));
        });
    }

    @Test
    public void testRestoreOrder() {
        // Given
        OrderId id = OrderId.of(1L);
        OrderName name = OrderName.of("Test Order");
        OrderStatus status = OrderStatus.CREATED;

        // When
        Order order = Order.restore(id, name, status);

        // Then
        assertNotNull(order);
        assertEquals(id, order.getId());
        assertEquals(name, order.getName());
        assertEquals(status, order.getStatus());
    }

    @Test
    public void testRestoreOrderWithNullId() {
        // Given
        OrderName name = OrderName.of("Test Order");
        OrderStatus status = OrderStatus.CREATED;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Order.restore(OrderId.of(null), name, status);
        });
    }

    @Test
    public void testRestoreOrderWithNegativeId() {
        // Given
        OrderName name = OrderName.of("Test Order");
        OrderStatus status = OrderStatus.CREATED;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Order.restore(OrderId.of(0L), name, status);
        });
    }

    @Test
    public void testRestoreOrderWithNullName() {
        // Given
        OrderId id = OrderId.of(1L);
        OrderStatus status = OrderStatus.CREATED;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Order.restore(id, OrderName.of(null), status);
        });
    }

    @Test
    public void testRestoreOrderWithEmptyName() {
        // Given
        OrderId id = OrderId.of(1L);
        OrderStatus status = OrderStatus.CREATED;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Order.restore(id, OrderName.of(""), status);
        });
    }

    @Test
    public void testRestoreOrderWithNullStatus() {
        // Given
        OrderId id = OrderId.of(1L);
        OrderName name = OrderName.of("Test Order");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Order.restore(id, name, OrderStatus.fromValue(null));
        });
    }

    @Test
    public void testRestoreOrderWithEmptyStatus() {
        // Given
        OrderId id = OrderId.of(1L);
        OrderName name = OrderName.of("Test Order");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Order.restore(id, name, OrderStatus.fromValue(""));
        });
    }
}