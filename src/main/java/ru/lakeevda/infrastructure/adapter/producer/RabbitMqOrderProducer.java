package ru.lakeevda.infrastructure.adapter.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import ru.lakeevda.application.dto.OrderParamResponse;
import ru.lakeevda.application.port.out.producer.OrderProducer;

@ApplicationScoped
public class RabbitMqOrderProducer implements OrderProducer {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @Channel("order-events-out")
    Emitter<String> emitter;

    @Override
    public void publish(OrderParamResponse paramOut) {
        Log.infof("Отправка сообщения: %s", paramOut);
        String json = toJson(paramOut);
        emitter.send(json);
        Log.infof("Сообщение отправлено");
    }

    private String toJson(OrderParamResponse paramOut) {
        try {
            return objectMapper.writeValueAsString(paramOut);
        } catch (Exception e) {
            Log.error(e);
            throw new RuntimeException("Ошибка серриализации сообщения", e);
        }
    }
}
