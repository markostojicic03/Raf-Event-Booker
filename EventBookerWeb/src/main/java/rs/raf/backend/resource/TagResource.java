package rs.raf.backend.resource;



import rs.raf.backend.model.TagModel;

import rs.raf.backend.repository.tag.MySqlTagRepository;

import rs.raf.backend.service.TagService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/tag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TagResource {
    private final TagService tagService = new TagService(new MySqlTagRepository());


    @GET
    public List<TagModel> getAllTags() {
        return tagService.getAlltags();
    }

    @GET
    @Path("/{id}")
    public Response getTagById(@PathParam("id") Long id) {
        TagModel tag = tagService.getTagById(id);
        if (tag == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tag not found with ID: " + id)
                    .build();
        }
        return Response.ok(tag).build();
    }


    @POST
    public Response createTag(TagModel tag) {
        TagModel created = tagService.createTag(tag);
        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTag(@PathParam("id") Long id, TagModel tag) {
        TagModel updated = tagService.updateTag(id, tag);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tag not found with ID: " + id)
                    .build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTag(@PathParam("id") Long id) {
        boolean deleted = tagService.deleteTag(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tag not found with ID: " + id)
                    .build();
        }
        return Response.noContent().build();
    }

}
