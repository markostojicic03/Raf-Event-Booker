package rs.raf.backend.repository.category;

import rs.raf.backend.model.CategoryModel;


import java.util.List;

public interface CategoryRepository {
    List<CategoryModel> findAll();
    CategoryModel findById(Long id);
    CategoryModel searchByName(String name);
    void save(CategoryModel category);
    void delete(Long id);
}
