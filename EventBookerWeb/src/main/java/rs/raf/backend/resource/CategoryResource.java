package rs.raf.backend.resource;


import rs.raf.backend.model.CategoryModel;
import rs.raf.backend.repository.category.CategoryRepository;
import rs.raf.backend.repository.category.MySqlCategoryRepository;
import rs.raf.backend.repository.event.MySqlEventRepository;
import rs.raf.backend.service.CategoryService;
import rs.raf.backend.service.EventService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryService categoryService = new CategoryService(new MySqlCategoryRepository());


    @GET
    public List<CategoryModel> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        Optional<CategoryModel> category = categoryService.getCategoryById(id);
        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found with ID: " + id)
                    .build();
        }
        return Response.ok(category).build();
    }

    @POST
    public Response createCategory(CategoryModel category) {
        CategoryModel created = categoryService.createCategory(category);
        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, CategoryModel category) {
        CategoryModel updated = categoryService.updateCategory(id, category);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found with ID: " + id)
                    .build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found with ID: " + id)
                    .build();
        }
        return Response.noContent().build();
    }
}
