package ru.lakeevda.unit.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.application.dto.OrderParamResponse;
import ru.lakeevda.application.port.out.producer.OrderProducer;
import ru.lakeevda.application.port.out.repository.OrderRepository;
import ru.lakeevda.application.usecase.OrderUseCaseImpl;
import ru.lakeevda.domain.model.order.Order;
import ru.lakeevda.domain.model.order.OrderId;
import ru.lakeevda.domain.model.order.OrderName;
import ru.lakeevda.domain.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class OrderUseCaseImplTest {

    @Mock
    OrderRepository repository;

    @Mock
    OrderProducer producer;

    @InjectMocks
    OrderUseCaseImpl orderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        Order order1 = Order.restore(OrderId.of(1L), OrderName.of("A"), OrderStatus.CREATED);
        Order order2 = Order.restore(OrderId.of(2L), OrderName.of("B"), OrderStatus.COMPLETED);
        when(repository.findAll()).thenReturn(List.of(order1, order2));
        List<OrderParamResponse> result = orderUseCase.getAll();
        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals("A", result.getFirst().name());
        assertEquals("CREATED", result.getFirst().status());
    }

    @Test
    public void testGetByIdFound() {
        Order order = Order.restore(OrderId.of(5L), OrderName.of("X"), OrderStatus.EDITED);
        when(repository.findById(anyLong())).thenReturn(Optional.of(order));

        OrderParamResponse resp = orderUseCase.getById(5L);
        assertEquals(5L, resp.id());
        assertEquals("X", resp.name());
        assertEquals("EDITED", resp.status());
    }

    @Test
    public void testGetByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderUseCase.getById(99L));
    }

    @Test
    public void testCreatePublish() {
        OrderParamRequest req = new OrderParamRequest("Order1", "CREATED");
        Order order = Order.restore(OrderId.of(10L), OrderName.of("Order1"), OrderStatus.CREATED);
        when(repository.persist(any(Order.class))).thenReturn(order);

        OrderParamResponse resp = orderUseCase.create(req, true);
        assertEquals(10L, resp.id());
        verify(producer, times(1)).publish(resp);
    }

    @Test
    public void testCreateNoPublish() {
        OrderParamRequest req = new OrderParamRequest("Order2", "COMPLETED");
        Order order = Order.restore(OrderId.of(20L), OrderName.of("Order2"), OrderStatus.COMPLETED);
        when(repository.persist(any(Order.class))).thenReturn(order);

        OrderParamResponse resp = orderUseCase.create(req, false);
        assertEquals(20L, resp.id());
        verify(producer, never()).publish(any());
    }

    @Test
    public void testDelete() {
        orderUseCase.delete(7L);
        verify(repository, times(1)).deleteById(7L);
    }
}
