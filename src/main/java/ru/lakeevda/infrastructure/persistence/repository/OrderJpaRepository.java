package ru.lakeevda.infrastructure.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.lakeevda.infrastructure.persistence.entity.OrderEntity;

@ApplicationScoped
public class OrderJpaRepository implements PanacheRepositoryBase<OrderEntity, Long> {
}