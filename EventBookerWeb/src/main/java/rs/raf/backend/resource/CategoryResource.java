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
import java.util.Map;
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
    @GET
    @Path("/all/{id}")
    public List<CategoryModel> getAllCategoriesById(@PathParam("id")  Long id) {
        return categoryService.getAllCategoriesById(id);
    }

    @POST
    public Response createCategory(CategoryModel dto) {
        if (categoryService.existsByNameIgnoreCase(dto.getCategoryName())) {
            Map<String, String> m = Map.of("message", "Category with this name already exists");
            return Response.status(Response.Status.CONFLICT).entity(m).build();
        }

        CategoryModel created = categoryService.createCategory(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public CategoryModel updateCategory(@PathParam("id") Long id, CategoryModel dto) {
        CategoryModel existing = categoryService.getCategoryById(id).orElse(null);
        if (existing == null) return null;

        existing.setCategoryName(dto.getCategoryName());
        existing.setCategoryDescription(dto.getCategoryDescription());
        categoryService.updateCategory(id, existing);
        return existing;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        boolean ok = categoryService.deleteCategory(id);
        if (!ok) {
            Map<String, String> m = Map.of("message",
                    "Brisanje nije dozvoljeno – postoje događaji u ovoj kategoriji.");
            return Response.status(Response.Status.CONFLICT).entity(m).build();
        }
        return Response.noContent().build();
    }
}
