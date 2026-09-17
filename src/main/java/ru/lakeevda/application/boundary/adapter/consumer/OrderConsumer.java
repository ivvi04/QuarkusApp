package ru.lakeevda.application.boundary.adapter.consumer;

public interface OrderConsumer<T> {
    void handle(T payload);
}
