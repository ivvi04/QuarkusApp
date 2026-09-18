package ru.lakeevda.application.port.out.producer;

import ru.lakeevda.application.dto.OrderParamResponse;

public interface OrderProducer {
    void publish(OrderParamResponse paramOut);
}
