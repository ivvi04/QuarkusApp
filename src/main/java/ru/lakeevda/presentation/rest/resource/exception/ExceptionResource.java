package ru.lakeevda.presentation.rest.resource.exception;

import io.quarkus.logging.Log;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.lakeevda.presentation.dto.ErrorResponse;

@Provider
public class ExceptionResource implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        Log.error(exception.getMessage(), exception);
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("NOT_FOUND", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
