package ru.lakeevda.application.port.in.usecase;

import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.application.dto.OrderParamResponse;

import java.util.List;

public interface OrderUseCase {
    List<OrderParamResponse> getAll();

    OrderParamResponse getById(Long id);

    OrderParamResponse create(OrderParamRequest param, Boolean publish);

    void delete(Long id);
}