package com.project.shopapp.controller;

import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.dto.CategoryDto;
import com.project.shopapp.model.Category;
import com.project.shopapp.response.BaseResponse;
import com.project.shopapp.service.CategoryService;
import com.project.shopapp.service.ICategoryService;
import com.project.shopapp.utils.BindingResultUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Validated
@RestController
@RequestMapping ("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    public ResponseEntity<BaseResponse> insertCategory (@RequestBody @Valid CategoryDto category, BindingResult bindingResult){
        ResponseEntity<BaseResponse> errorMsg = BindingResultUtils.getResponseEntity(localizationUtils, bindingResult);
        if (errorMsg != null) return errorMsg;
        try{
            categoryService.createCategory(category);
            return ResponseEntity.ok().body(BaseResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_SUCCESS))
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_FAILED, e.getMessage()))
                    .build());
        }

    }
    @GetMapping("")
    public ResponseEntity<?> getCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        try {
            List<Category> categories = categoryService.getAllCategory();
            return ResponseEntity.ok().body(BaseResponse.builder().data(categories).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable Integer id,
            @RequestBody CategoryDto categoryDto
    ) {
        try {
            Category category = categoryService.updateCategory(id, categoryDto);
            return ResponseEntity.ok().body("update category successfully" + category);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Integer id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body("delete category successfully");
    }
}
