package ru.lakeevda.infrastructure.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
public class OrderEntity extends PanacheEntity {
    public Long id;
    public String name;
    public String status;
}
