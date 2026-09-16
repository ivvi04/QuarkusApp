package ru.lakeevda.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.lakeevda.domain.entity.Order;
import ru.lakeevda.domain.repository.OrderRepository;
import ru.lakeevda.domain.usecase.OrderUseCase;

import java.util.List;

@ApplicationScoped
public class OrderUseCaseImpl implements OrderUseCase {

    @Inject
    OrderRepository repository;

    @Override
    public List<Order> getAll() {
        return repository.findAll();
    }
    
    @Override
    public Order getById(Long id) {
        return repository.findById(id).orElseThrow();
    }
    
    @Override
    public Order create(Order order) {
        return repository.save(order);
    }
    
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}