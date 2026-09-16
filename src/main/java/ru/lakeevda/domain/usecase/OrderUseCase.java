package ru.lakeevda.domain.usecase;

import ru.lakeevda.domain.entity.Order;

import java.util.List;

public interface OrderUseCase {
    List<Order> getAll();

    Order getById(Long id);

    Order create(Order order);

    void delete(Long id);
}