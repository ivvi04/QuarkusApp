package ru.lakeevda.presentation.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.lakeevda.domain.boundary.model.order.OrderRequest;
import ru.lakeevda.domain.boundary.model.order.OrderResponse;
import ru.lakeevda.domain.boundary.usecase.OrderUseCase;

import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderUseCase orderUseCase;

    @GET
    public List<OrderResponse> getAllOrders() {
        return orderUseCase.getAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(orderUseCase.getById(id)).build();
    }

    @POST
    public Response createOrder(OrderRequest request) {
        OrderResponse createdOrder = orderUseCase.create(request, Boolean.TRUE);
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