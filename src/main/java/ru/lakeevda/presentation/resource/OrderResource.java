package ru.lakeevda.presentation.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.lakeevda.domain.entity.Order;
import ru.lakeevda.domain.usecase.OrderUseCase;

import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderUseCase orderUseCase;

    @GET
    public List<Order> getAllOrders() {
        return orderUseCase.getAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(orderUseCase.getById(id)).build();
    }

    @POST
    public Response createOrder(Order order) {
        Order createdOrder = orderUseCase.create(order);
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