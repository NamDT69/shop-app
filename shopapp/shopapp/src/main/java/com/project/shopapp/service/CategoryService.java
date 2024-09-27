package com.project.shopapp.service;

import com.project.shopapp.dto.CategoryDto;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.model.Category;
import com.project.shopapp.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;
    @Override
    public Category createCategory(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(Integer id, CategoryDto category) {
        // Retrieve the existing category
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        // Validate the category
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }

        // Update the category
        mapper.map(category, existingCategory);

        // Save the updated category
        Category updatedCategory = categoryRepository.save(existingCategory);

        // Return the updated category
        return updatedCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
