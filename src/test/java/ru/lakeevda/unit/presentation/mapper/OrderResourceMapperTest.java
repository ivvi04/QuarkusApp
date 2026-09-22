package ru.lakeevda.unit.presentation.mapper;

import org.junit.jupiter.api.Test;
import ru.lakeevda.application.dto.OrderParamRequest;
import ru.lakeevda.application.dto.OrderParamResponse;
import ru.lakeevda.presentation.dto.OrderRequest;
import ru.lakeevda.presentation.dto.OrderResponse;
import ru.lakeevda.presentation.mapper.OrderResourceMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderResourceMapperTest {
    @Test
    public void testToParam() {
        OrderRequest req = new OrderRequest("TestName", "CREATED");
        OrderParamRequest param = OrderResourceMapper.toParam(req);
        assertEquals("TestName", param.name());
        assertEquals("CREATED", param.status());
    }

    @Test
    public void testFromParam() {
        OrderParamResponse out = new OrderParamResponse(5L, "OutName", "COMPLETED");
        OrderResponse resp = OrderResourceMapper.fromParam(out);
        assertEquals(5L, resp.id());
        assertEquals("OutName", resp.name());
        assertEquals("COMPLETED", resp.status());
    }
}
