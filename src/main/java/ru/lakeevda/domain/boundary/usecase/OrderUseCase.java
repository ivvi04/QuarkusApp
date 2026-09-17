package ru.lakeevda.domain.boundary.usecase;

import ru.lakeevda.domain.boundary.model.order.OrderRequest;
import ru.lakeevda.domain.boundary.model.order.OrderResponse;

import java.util.List;

public interface OrderUseCase {
    List<OrderResponse> getAll();

    OrderResponse getById(Long id);

    OrderResponse create(OrderRequest order, Boolean publish);

    void delete(Long id);
}