package dev.mednikov.cashtracker.categories.dto.mappers;

import dev.mednikov.cashtracker.categories.dto.CategoryDto;
import dev.mednikov.cashtracker.categories.models.Category;

import java.util.function.Function;

public final class CategoryDtoMapper implements Function<Category, CategoryDto> {

    @Override
    public CategoryDto apply(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getOwner().getId(),
                category.getName(),
                category.getDescription(),
                category.getColor()
        );
    }
}
