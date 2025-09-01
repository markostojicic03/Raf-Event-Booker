package rs.raf.backend.filth;

import rs.raf.backend.repository.user.MySqlUserRepository;
import rs.raf.backend.service.UserService;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;


@Provider
public class AuthFilter implements ContainerRequestFilter {

    private final UserService userService = new UserService(new MySqlUserRepository());


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (!this.isAuthRequired(requestContext)) {
            return;
        }

        try {
            String token = requestContext.getHeaderString("Authorization");
            if(token != null && token.startsWith("Bearer ")) {
                token = token.replace("Bearer ", "");
            }

            if (!this.userService.isAuthorized(token)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } catch (Exception exception) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private boolean isAuthRequired(ContainerRequestContext req) {
        String path = req.getUriInfo().getPath();

        if (path.contains("login")) {
            return false;
        }

        return path.startsWith("api/admin") || path.startsWith("api/ems");
    }
}
