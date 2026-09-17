package ru.lakeevda.domain.boundary.repository;

import ru.lakeevda.domain.entity.order.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();
    Optional<Order> findById(Long id);
    Order persist(Order order);
    void deleteById(Long id);
}