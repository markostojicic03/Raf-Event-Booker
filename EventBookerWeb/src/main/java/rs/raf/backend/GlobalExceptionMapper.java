package rs.raf.backend;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;


@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception ex) {
        ex.printStackTrace();               // console
        return Response.status(500)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}
