package ru.lakeevda.application.repository.order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.lakeevda.domain.entity.order.Order;
import ru.lakeevda.domain.boundary.repository.OrderRepository;
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
                .map(OrderRepositoryMapper::fromEntity)
                .toList();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findByIdOptional(id)
                .map(OrderRepositoryMapper::fromEntity);
    }

    @Transactional
    @Override
    public Order persist(Order order) {
        var OrderEntity = OrderRepositoryMapper.toEntity(order);
        jpaRepository.persist(OrderEntity);
        return OrderRepositoryMapper.fromEntity(OrderEntity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}