package ru.lakeevda.infrastructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.lakeevda.infrastructure.entity.OrderEntity;

@ApplicationScoped
public class OrderJpaRepository implements PanacheRepository<OrderEntity> {
}