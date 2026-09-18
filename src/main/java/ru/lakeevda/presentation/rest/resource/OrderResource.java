package ru.lakeevda.presentation.rest.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.lakeevda.application.dto.OrderParamResponse;
import ru.lakeevda.application.port.in.usecase.OrderUseCase;
import ru.lakeevda.presentation.dto.OrderRequest;
import ru.lakeevda.presentation.dto.OrderResponse;
import ru.lakeevda.presentation.mapper.OrderResourceMapper;

import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderUseCase orderUseCase;

    @GET
    public List<OrderResponse> getAllOrders() {
        return orderUseCase.getAll().stream().map(OrderResourceMapper::fromParam).toList();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(orderUseCase.getById(id)).build();
    }

    @POST
    public Response createOrder(OrderRequest request) {
        OrderParamResponse paramOut = orderUseCase.create(OrderResourceMapper.toParam(request), Boolean.TRUE);
        OrderResponse createdOrder = OrderResourceMapper.fromParam(paramOut);
        return Response.status(Response.Status.CREATED)
                .entity(createdOrder).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") Long id) {
        orderUseCase.delete(id);
        return Response.noContent().build();
    }
}