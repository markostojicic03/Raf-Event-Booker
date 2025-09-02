package rs.raf.backend.resource;

import rs.raf.backend.model.UserModel;
import rs.raf.backend.repository.user.MySqlUserRepository;
import rs.raf.backend.requests.LoginRequest;
import rs.raf.backend.service.UserService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            userService.createUser(user);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(Map.of("error", ex.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(@Valid LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();

        String jwt = this.userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (jwt == null) {
            response.put("message", "These credentials do not match our records");
            return Response.status(422).entity(response).build();
        }

        response.put("jwt", jwt);
        response.put("role", userService.getUserByEmail(loginRequest.getUsername())
                .orElseThrow()
                .getRole());

        return Response.ok(response).build();
    }


    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, UserModel user) {
        try {
            userService.updateUser(user);
            return Response.ok().build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).entity(Map.of("error", ex.getMessage())).build();
        }
    }
}
