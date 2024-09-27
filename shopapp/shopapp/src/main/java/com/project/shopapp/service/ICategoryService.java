package com.project.shopapp.service;

import com.project.shopapp.dto.CategoryDto;
import com.project.shopapp.model.Category;
import com.project.shopapp.repository.CategoryRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategoryService {


    Category createCategory(CategoryDto categoryDto);

    Category getCategoryById(Integer id);
    List<Category> getAllCategory();

    @Transactional
    Category updateCategory(Integer id, CategoryDto category);

    void deleteCategory(Integer id);
}
