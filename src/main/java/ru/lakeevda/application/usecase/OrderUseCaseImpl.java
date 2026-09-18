package ru.lakeevda.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.lakeevda.application.mapper.OrderUseCaseMapper;
import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.application.dto.OrderParamResponse;
import ru.lakeevda.application.port.in.usecase.OrderUseCase;
import ru.lakeevda.application.port.out.producer.OrderProducer;
import ru.lakeevda.application.port.out.repository.OrderRepository;

import java.util.List;

@ApplicationScoped
public class OrderUseCaseImpl implements OrderUseCase {

    @Inject
    OrderRepository repository;
    @Inject
    OrderProducer producer;

    @Override
    public List<OrderParamResponse> getAll() {
        return repository.findAll().stream()
                .map(OrderUseCaseMapper::fromDomain)
                .toList();
    }

    @Override
    public OrderParamResponse getById(Long id) {
        return repository.findById(id)
                .map(OrderUseCaseMapper::fromDomain)
                .orElseThrow();
    }

    @Override
    public OrderParamResponse create(OrderParamRequest paramRequest, Boolean publish) {
        var order = OrderUseCaseMapper.toDomain(paramRequest);
        order = repository.persist(order);
        var paramResponse = OrderUseCaseMapper.fromDomain(order);

        if (publish) producer.publish(paramResponse);

        return paramResponse;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}