package ru.lakeevda.application.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.lakeevda.domain.entity.Order;
import ru.lakeevda.domain.repository.OrderRepository;
import ru.lakeevda.infrastructure.repository.OrderJpaRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderRepositoryImpl implements OrderRepository {

    @Inject
    OrderJpaRepository jpaRepository;

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(orderEntity -> Order.restore(orderEntity.id, orderEntity.name, orderEntity.status))
                .toList();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findByIdOptional(id)
                .map(orderEntity -> Order.restore(orderEntity.id, orderEntity.name, orderEntity.status));
    }

    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}