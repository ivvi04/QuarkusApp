package ru.lakeevda.infrastructure.adapter.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import ru.lakeevda.application.boundary.adapter.producer.OrderProducer;
import ru.lakeevda.domain.boundary.model.order.OrderResponse;

@ApplicationScoped
public class RabbitMqOrderProducer implements OrderProducer {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @Channel("order-events-out")
    Emitter<String> emitter;

    @Override
    public void publish(OrderResponse order) {
        Log.infof("Отправка сообщения: %s", order);
        String json = toJson(order);
        emitter.send(json);
        Log.infof("Сообщение отправлено");
    }

    private String toJson(OrderResponse order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (Exception e) {
            Log.error(e);
            throw new RuntimeException("Ошибка серриализации сообщения", e);
        }
    }
}
