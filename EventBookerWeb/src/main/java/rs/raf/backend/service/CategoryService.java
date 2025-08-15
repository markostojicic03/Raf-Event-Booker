package rs.raf.backend.service;

import rs.raf.backend.model.CategoryModel;
import rs.raf.backend.repository.category.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryModel> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<CategoryModel> getCategoryById(Long id) {
        return Optional.ofNullable(categoryRepository.findById(id));
    }

    public CategoryModel createCategory(CategoryModel category) {
        categoryRepository.save(category);
        return category;
    }

    public CategoryModel updateCategory(Long id, CategoryModel updatedCategory) {
        CategoryModel existing = categoryRepository.findById(id);
        if (existing != null) {
            existing.setCategoryName(updatedCategory.getCategoryName());
            existing.setCategoryDescription(updatedCategory.getCategoryDescription());
            categoryRepository.save(existing);
        }
        return null; // ili baci exception
    }

    public boolean deleteCategory(Long id) {
        CategoryModel existing = categoryRepository.findById(id);
        if (existing != null) {
            categoryRepository.delete(existing.getId());
            return true;
        }
        return false;
    }
}
