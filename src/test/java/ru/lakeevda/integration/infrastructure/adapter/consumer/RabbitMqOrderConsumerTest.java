package ru.lakeevda.integration.infrastructure.adapter.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.infrastructure.persistence.repository.OrderJpaRepository;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@QuarkusTest
public class RabbitMqOrderConsumerTest {

    private static final String EXCHANGE = "orders-exchange";
    private static final String ROUTING_KEY = "order.create";

    private final AMQP.BasicProperties PROPS = new AMQP.BasicProperties.Builder()
            .contentType("application/json")
            .build();

    @ConfigProperty(name = "rabbitmq-host")
    String host;

    @ConfigProperty(name = "rabbitmq-port")
    int port;

    @ConfigProperty(name = "rabbitmq-username")
    String username;

    @ConfigProperty(name = "rabbitmq-password")
    String password;

    private Connection connection;
    private Channel channel;

    @Inject
    ObjectMapper mapper;

    @Inject
    OrderJpaRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        connection = factory.newConnection();
        channel = connection.createChannel();

        QuarkusTransaction.requiringNew().run(() -> {
            repository.deleteAll();
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        if (channel != null && channel.isOpen()) channel.close();
        if (connection != null && connection.isOpen()) connection.close();
    }

    @Test
    void testConsumeSuccess() throws Exception {
        publish(new OrderParamRequest("TestOrder", "CREATED"));

        await().atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    boolean exists = QuarkusTransaction.requiringNew().call(() ->
                            repository.findAll().stream()
                                    .anyMatch(o -> "TestOrder".equals(o.getName())));
                    assertTrue(exists, "Заказ 'TestOrder' должен быть сохранён");
                });
    }

    @Test
    void testConsumeWithEmptyData() throws Exception {
        publish(new OrderParamRequest("", ""));

        await().atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    long count = QuarkusTransaction.requiringNew().call(() ->
                            repository.findAll().count());
                    assertEquals(0, count, "Заказ с пустым name не должен быть сохранён");
                });
    }

    @Test
    void testConsumeWithNullData() throws Exception {
        publish(new OrderParamRequest(null, null));

        await().atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    long count = QuarkusTransaction.requiringNew().call(() ->
                            repository.findAll().count());
                    assertEquals(0, count, "Заказ с null name не должен быть сохранён");
                });
    }

    @Test
    void testConsumeWithInvalidMessage() throws Exception {
        var json = "{\"notName\":\"TestOrder\"}";

        channel.basicPublish("", ROUTING_KEY, PROPS, mapper.writeValueAsBytes(json));

        await().atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    long count = QuarkusTransaction.requiringNew().call(() ->
                            repository.findAll().count());
                    assertEquals(0, count, "Заказ с неверным форматом не должен быть сохранён");
                });
    }


    @Test
    void testConsumeWithInvalidJson() throws Exception {
        // Отправляем НЕвалидный JSON → Jackson выбросит JsonParseException
        var invalidJson = "{bad json without closing brace".getBytes();
        channel.basicPublish("", ROUTING_KEY, PROPS, invalidJson);

        await().atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    long count = QuarkusTransaction.requiringNew().call(() ->
                            repository.findAll().count());
                    assertEquals(0, count, "Заказ с невалидным JSON не должен быть сохранён");
                });
    }

    private void publish(OrderParamRequest request) throws Exception {
        var json = mapper.writeValueAsBytes(request);
        channel.basicPublish(EXCHANGE, ROUTING_KEY, PROPS, json);
    }

}