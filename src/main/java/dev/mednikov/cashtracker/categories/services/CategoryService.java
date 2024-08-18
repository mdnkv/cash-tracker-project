package dev.mednikov.cashtracker.categories.services;

import dev.mednikov.cashtracker.categories.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryDto createCategory (CategoryDto categoryDto);

    CategoryDto updateCategory (CategoryDto categoryDto);

    void deleteCategory (Long id);

    Optional<CategoryDto> getCategory (Long id);

    List<CategoryDto> getAllCategories(Long ownerId);

}
