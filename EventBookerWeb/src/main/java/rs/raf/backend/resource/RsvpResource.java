package rs.raf.backend.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.auth0.jwt.exceptions.JWTDecodeException;
import rs.raf.backend.model.CommentModel;
import rs.raf.backend.model.EventModel;
import rs.raf.backend.model.UserModel;
import rs.raf.backend.repository.category.MySqlCategoryRepository;
import rs.raf.backend.repository.comment.MySqlCommentRepository;
import rs.raf.backend.repository.event.MySqlEventRepository;
import rs.raf.backend.repository.eventregistration.MySqlRsvpRepository;
import rs.raf.backend.repository.tag.MySqlTagRepository;
import rs.raf.backend.repository.user.MySqlUserRepository;
import rs.raf.backend.service.CommentService;
import rs.raf.backend.service.EventService;
import rs.raf.backend.service.RsvpService;
import rs.raf.backend.service.UserService;
import rs.raf.backend.utils.JwtUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RsvpResource {
    private final RsvpService rsvpService = new RsvpService(new MySqlRsvpRepository());
    private final EventService eventService = new EventService(new MySqlEventRepository(), new MySqlTagRepository(), new MySqlCategoryRepository());


    @GET
    @Path("/{id}/rsvp/count")
    public int rsvpCount(@PathParam("id") Long id){ return rsvpService.countByEvent(id); }

    @POST
    @Path("/{id}/rsvp")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rsvp(@PathParam("id") Long id,
                         @HeaderParam("Authorization") String auth,
                         Map<String,String> body) {

        String userEmail = body.get("email");
        if (auth != null && auth.startsWith("Bearer ")) {

            userEmail = JwtUtil.extractUsername(auth.substring(7));
        }
        if (userEmail == null || userEmail.isBlank())
            return Response.status(400).entity(Map.of("error","Email obavezan.")).build();

        EventModel ev = eventService.getEvent(id);
        if (ev == null) return Response.status(404).build();
        if (ev.getMaxCapacity() != null && ev.getMaxCapacity() > 0 &&
                rsvpService.countByEvent(id) >= ev.getMaxCapacity())
            return Response.status(409).entity(Map.of("error","Kapacitet popunjen.")).build();

        try{
            rsvpService.register(id, userEmail);
            return Response.ok(Map.of("count", rsvpService.countByEvent(id))).build();
        }catch(IllegalStateException e){
            return Response.status(409).entity(Map.of("error", e.getMessage())).build();
        }
    }



}
