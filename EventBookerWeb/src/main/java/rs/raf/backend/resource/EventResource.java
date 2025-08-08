package rs.raf.backend.resource;


import rs.raf.backend.model.EventModel;
import rs.raf.backend.repository.event.MySqlEventRepository;
import rs.raf.backend.service.EventService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    private EventService eventService = new EventService(new MySqlEventRepository());

    // Vrati sve događaje
    @GET
    public List<EventModel> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Vrati događaj po ID-u
    @GET
    @Path("/{id}")
    public EventModel getEvent(@PathParam("id") Long id) {
        return eventService.getEvent(id);
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
