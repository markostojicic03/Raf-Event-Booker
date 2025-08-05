package rs.raf.backend.resource;

import rs.raf.backend.model.UserModel;
import rs.raf.backend.repository.user.MySqlUserRepository;
import rs.raf.backend.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private UserService userService = new UserService(new MySqlUserRepository());

    @GET
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    public UserModel getUser(@PathParam("id") Long id) {
        return userService.getUser(id);
    }

    @POST
    public Response createUser(UserModel user) {
        userService.createUser(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


}
