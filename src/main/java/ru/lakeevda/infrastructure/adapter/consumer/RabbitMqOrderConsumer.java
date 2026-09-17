package ru.lakeevda.infrastructure.adapter.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import ru.lakeevda.application.boundary.adapter.consumer.OrderConsumer;
import ru.lakeevda.domain.boundary.model.order.OrderRequest;
import ru.lakeevda.domain.boundary.usecase.OrderUseCase;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class RabbitMqOrderConsumer implements OrderConsumer<OrderRequest> {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    OrderUseCase orderUseCase;

    @Incoming("order-events-in")
    @Blocking
    public CompletionStage<Void> consume(Message<byte[]> message) {
        try {
            Log.infof("Обработка сообщения: {}", message);
            var order = objectMapper.readValue(message.getPayload(), OrderRequest.class);
            handle(order);
            Log.infof("Сообщение успешно обработано: {}", order);
            return message.ack();
        } catch (Exception e) {
            Log.error(e);
            throw new RuntimeException("Ошибка обработки полученного заказа", e);
        }
    }

    @Override
    public void handle(OrderRequest request) {
        orderUseCase.create(request, Boolean.FALSE);
    }
}
