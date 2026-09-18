package ru.lakeevda.presentation.mapper;

import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.application.dto.OrderParamResponse;
import ru.lakeevda.presentation.dto.OrderRequest;
import ru.lakeevda.presentation.dto.OrderResponse;

public class OrderResourceMapper {

    public static OrderParamRequest toParam(OrderRequest orderRequest) {
        return new OrderParamRequest(orderRequest.name(), orderRequest.status());
    }

    public static OrderResponse fromParam(OrderParamResponse orderParamOut) {
        return new OrderResponse(orderParamOut.id(), orderParamOut.name(), orderParamOut.status());
    }
}
