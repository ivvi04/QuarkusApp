package ru.lakeevda.unit.infrastructure.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.lakeevda.domain.model.order.Order;
import ru.lakeevda.domain.model.order.OrderId;
import ru.lakeevda.domain.model.order.OrderName;
import ru.lakeevda.domain.model.order.OrderStatus;
import ru.lakeevda.infrastructure.persistence.entity.OrderEntity;
import ru.lakeevda.infrastructure.persistence.repository.OrderJpaRepository;
import ru.lakeevda.infrastructure.persistence.repository.OrderRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderRepositoryImplTest {

    @Mock
    private OrderJpaRepository jpaRepository;

    @InjectMocks
    private OrderRepositoryImpl repository;

    private OrderEntity entity1;
    private OrderEntity entity2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        entity1 = new OrderEntity();
        entity1.setId(1L);
        entity1.setName("Order1");
        entity1.setStatus("CREATED");

        entity2 = new OrderEntity();
        entity2.setId(2L);
        entity2.setName("Order2");
        entity2.setStatus("COMPLETED");
    }

    @Test
    void testFindAll() {
        var query = mock(PanacheQuery.class);
        when(query.stream()).thenReturn(Stream.of(entity1, entity2));
        when(jpaRepository.findAll()).thenReturn(query);

        List<Order> result = repository.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().getId().getValue());
        assertEquals("Order1", result.getFirst().getName().getValue());
        assertEquals("CREATED", result.getFirst().getStatus().getValue());
    }

    @Test
    void testFindByIdFound() {
        when(jpaRepository.findByIdOptional(1L)).thenReturn(Optional.of(entity1));

        Optional<Order> opt = repository.findById(1L);
        assertTrue(opt.isPresent());
        Order order = opt.get();
        assertEquals(1L, order.getId().getValue());
        assertEquals("Order1", order.getName().getValue());
    }

    @Test
    void testFindByIdNotFound() {
        when(jpaRepository.findByIdOptional(99L)).thenReturn(Optional.empty());
        Optional<Order> opt = repository.findById(99L);
        assertFalse(opt.isPresent());
    }

    @Test
    void testPersist() {
        Order order = Order.restore(OrderId.of(10L), OrderName.of("Order10"), OrderStatus.CREATED);

        Order persisted = repository.persist(order);
        assertEquals(10L, persisted.getId().getValue());
        assertEquals("Order10", persisted.getName().getValue());
    }

    @Test
    void testDeleteById() {
        when(jpaRepository.deleteById(5L)).thenReturn(Boolean.TRUE);
        repository.deleteById(5L);
        verify(jpaRepository, atLeastOnce()).deleteById(5L);
    }
}
