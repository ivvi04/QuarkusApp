package ru.lakeevda.application.port.in.consumer;

public interface OrderConsumer<T> {
    void handle(T payload);
}
