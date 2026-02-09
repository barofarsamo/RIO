package com.riyobox.service;

import com.riyobox.model.Category;
import com.riyobox.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
    
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
    
    @Transactional
    public Category createCategory(Category category) {
        // Check if category already exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category already exists");
        }
        
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        
        return categoryRepository.save(category);
    }
    
    @Transactional
    public Category updateCategory(String id, Category categoryDetails) {
        Category category = getCategoryById(id);
        
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setIcon(categoryDetails.getIcon());
        category.setMovieCount(categoryDetails.getMovieCount());
        category.setUpdatedAt(LocalDateTime.now());
        
        return categoryRepository.save(category);
    }
    
    @Transactional
    public void deleteCategory(String id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
    
    @Transactional
    public void incrementMovieCount(String categoryName) {
        Category category = getCategoryByName(categoryName);
        category.setMovieCount(category.getMovieCount() + 1);
        categoryRepository.save(category);
    }
    
    @Transactional
    public void decrementMovieCount(String categoryName) {
        Category category = getCategoryByName(categoryName);
        if (category.getMovieCount() > 0) {
            category.setMovieCount(category.getMovieCount() - 1);
            categoryRepository.save(category);
        }
    }
}
