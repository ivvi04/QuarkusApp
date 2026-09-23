package ru.lakeevda.infrastructure.adapter.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.application.port.in.consumer.OrderConsumer;
import ru.lakeevda.application.port.in.usecase.OrderUseCase;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class RabbitMqOrderConsumer implements OrderConsumer<OrderParamRequest> {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    OrderUseCase orderUseCase;

    @Incoming("order-events-create")
    @Blocking
    public CompletionStage<Void> consume(Message<byte[]> message) {
        try {
            var order = objectMapper.readValue(message.getPayload(), OrderParamRequest.class);
            Log.infof("Обработка заказа: %s", order);
            handle(order);
            Log.info("Заказ успешно обработан");
            return message.ack();
        } catch (Exception e) {
            Log.errorf(e, "Ошибка валидации или десериализации сообщения");
            return message.nack(e);
        }
    }

    @Override
    public void handle(OrderParamRequest paramIn) {
        orderUseCase.create(paramIn, Boolean.FALSE);
    }
}
