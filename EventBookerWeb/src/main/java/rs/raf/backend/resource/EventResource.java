package rs.raf.backend.resource;


import rs.raf.backend.model.EventModel;
import rs.raf.backend.repository.event.MySqlEventRepository;
import rs.raf.backend.repository.tag.MySqlTagRepository;
import rs.raf.backend.repository.user.MySqlUserRepository;
import rs.raf.backend.service.EventService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {


    private final EventService eventService = new EventService(new MySqlEventRepository(), new MySqlTagRepository());

    // Vrati sve događaje
    @GET
    public List<EventModel> getAllEvents() {
        System.out.println("getAllEvents");
        return eventService.getAllEvents();
    }

    // Vrati događaj po ID-u
    @GET
    @Path("/{id}")
    public EventModel getEvent(@PathParam("id") Long id) {
        return eventService.getEvent(id);
    }


    @GET
    @Path("/all/{id}")
    public List<EventModel> getAllEventsByCategoryId(@PathParam("id") Long id) {
        return eventService.getAllEventsByCategoryId(id);
    }

    @POST
    @Path("/{id}/view")
    public Response incrementView(
            @PathParam("id") Long id,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) {

        final String cookieName = "view_" + id;
        boolean alreadyCounted = false;

        // 1. check existing cookie
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie c : request.getCookies()) {
                if (cookieName.equals(c.getName())) {
                    alreadyCounted = true;
                    break;
                }
            }
        }

        // 2. if first visit → increment + drop cookie
        if (!alreadyCounted) {
            eventService.incrementViewCount(id);   // add this method in EventService
            javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(cookieName, "1");
            cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return Response.ok().build();
    }

    @POST
    @Path("/{id}/like")
    public Response like(@PathParam("id") Long id,
                         @Context HttpServletRequest req,
                         @Context HttpServletResponse res) {
        return vote(id, "like", req, res);
    }

    @POST
    @Path("/{id}/dislike")
    public Response dislike(@PathParam("id") Long id,
                            @Context HttpServletRequest req,
                            @Context HttpServletResponse res) {
        return vote(id, "dislike", req, res);
    }

    private Response vote(Long id, String type, HttpServletRequest req, HttpServletResponse res) {
        String cookieName = "event_" + type + "_" + id;
        boolean alreadyVoted = false;

        if (req.getCookies() != null) {
            for (javax.servlet.http.Cookie c : req.getCookies()) {
                if (cookieName.equals(c.getName())) {
                    alreadyVoted = true;
                    break;
                }
            }
        }

        if (!alreadyVoted) {
            eventService.vote(id, type);          // UPDATE event SET likes = likes + 1 …
            javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(cookieName, "1");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            res.addCookie(cookie);
        }

        return Response.ok().build();
    }

    @GET
    @Path("/search")
    public List<EventModel> getAllEventsBySearch(@QueryParam("q")String querySearch) {
        return eventService.getAllEventsBySearch(querySearch);
    }

    // Vrati 10 najskorijih događaja
    @GET
    @Path("/latest")
    public List<EventModel> getLatestEvents() {
        return eventService.getLatestEvents(10);
    }

    // Kreiraj novi događaj
    @POST
    public Response createEvent(EventModel event) {
        eventService.createEvent(event);
        return Response.status(Response.Status.CREATED).build();
    }

    // Obriši događaj po ID-u
    @DELETE
    @Path("/{id}")
    public Response deleteEvent(@PathParam("id") Long id) {
        eventService.deleteEvent(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
