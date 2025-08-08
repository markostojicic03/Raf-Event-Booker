package rs.raf.backend;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class OptionsEndpoint {

    @OPTIONS
    @Path("{path: .*}")
    public Response options() {
        return Response.ok().build();
    }
}
