package ru.lakeevda.application.usecase.order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.lakeevda.application.boundary.adapter.producer.OrderProducer;
import ru.lakeevda.domain.boundary.model.order.OrderRequest;
import ru.lakeevda.domain.boundary.model.order.OrderResponse;
import ru.lakeevda.domain.boundary.repository.OrderRepository;
import ru.lakeevda.domain.boundary.usecase.OrderUseCase;

import java.util.List;

@ApplicationScoped
public class OrderUseCaseImpl implements OrderUseCase {

    @Inject
    OrderRepository repository;
    @Inject
    OrderProducer producer;

    @Override
    public List<OrderResponse> getAll() {
        return repository.findAll().stream()
                .map(OrderUseCaseMapper::fromEntity)
                .toList();
    }
    
    @Override
    public OrderResponse getById(Long id) {
        return repository.findById(id)
                .map(OrderUseCaseMapper::fromEntity)
                .orElseThrow();
    }
    
    @Override
    public OrderResponse create(OrderRequest request, Boolean publish) {
        var order = OrderUseCaseMapper.toEntity(request);
        order = repository.persist(order);
        var response = OrderUseCaseMapper.fromEntity(order);

        if (publish) producer.publish(response);

        return response;
    }
    
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}