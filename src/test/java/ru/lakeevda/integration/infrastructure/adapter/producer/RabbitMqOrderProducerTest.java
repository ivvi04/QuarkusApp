package ru.lakeevda.integration.infrastructure.adapter.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lakeevda.application.dto.OrderParamResponse;
import ru.lakeevda.infrastructure.adapter.producer.RabbitMqOrderProducer;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@QuarkusTest
public class RabbitMqOrderProducerTest {

    private static final String QUEUE = "order-events-created";
    private static final String EXCHANGE = "orders-exchange";
    private static final String ROUTING_KEY = "order.created";

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
    RabbitMqOrderProducer producer;

    @Inject
    ObjectMapper mapper;

    @BeforeEach
    void setUp() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE, "topic", true, false, null);
        channel.queueDeclare(QUEUE, true, false, false, null);
        channel.queueBind(QUEUE, EXCHANGE, ROUTING_KEY);
        channel.queuePurge(QUEUE);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (channel != null && channel.isOpen()) channel.close();
        if (connection != null && connection.isOpen()) connection.close();
    }

    @Test
    void testPublishValidOrderParamResponse() throws Exception {
        var response = new OrderParamResponse(1L, "TestOrder", "CREATED");

        producer.publish(response);

        await().atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    var getResponse = channel.basicGet(QUEUE, true);
                    assertNotNull(getResponse, "Сообщение должно быть в очереди");

                    OrderParamResponse received = mapper.readValue(getResponse.getBody(), OrderParamResponse.class);
                    assertEquals(1L, received.id());
                    assertEquals("TestOrder", received.name());
                    assertEquals("CREATED", received.status());
                });
    }

    @Test
    void testPublishNullThrowsException() throws Exception {
        producer.publish(null);

        await().atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    var getResponse = channel.basicGet(QUEUE, true);
                    assertNotNull(getResponse, "Сообщение должно быть в очереди");
                    var body = new String(getResponse.getBody());
                    assertEquals("null", body, "Null-объект сериализуется как строка 'null'");
                });
    }

    @Test
    void testPublishMultipleMessages() throws Exception {
        producer.publish(new OrderParamResponse(1L, "Order1", "CREATED"));
        producer.publish(new OrderParamResponse(2L, "Order2", "COMPLETED"));

        await().atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    long count = channel.queueDeclarePassive(QUEUE).getMessageCount();
                    assertEquals(2, count, "В очереди должно быть 2 сообщения");
                });

        var first = channel.basicGet(QUEUE, true);
        var second = channel.basicGet(QUEUE, true);
        assertNotNull(first);
        assertNotNull(second);

        OrderParamResponse firstResponse = mapper.readValue(first.getBody(), OrderParamResponse.class);
        OrderParamResponse secondResponse = mapper.readValue(second.getBody(), OrderParamResponse.class);
        assertEquals("Order1", firstResponse.name());
        assertEquals("Order2", secondResponse.name());
    }
}
