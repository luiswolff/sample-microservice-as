package de.wolff.sample;

import javax.enterprise.context.RequestScoped;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@RequestScoped
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.ok("Received invalid entity", MediaType.TEXT_PLAIN).build();
    }
}
