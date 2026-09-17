package ru.lakeevda.infrastructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.lakeevda.infrastructure.entity.OrderEntity;

@ApplicationScoped
public class OrderJpaRepository implements PanacheRepositoryBase<OrderEntity, Long> {
}