package dev.mednikov.cashtracker.categories.services;

import dev.mednikov.cashtracker.categories.dto.CategoryDto;
import dev.mednikov.cashtracker.categories.dto.mappers.CategoryDtoMapper;
import dev.mednikov.cashtracker.categories.models.Category;
import dev.mednikov.cashtracker.categories.repositories.CategoryRepository;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDtoMapper mapper = new CategoryDtoMapper();

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        User owner = this.userRepository.getReferenceById(categoryDto.ownerId());
        Category category = new Category.CategoryBuilder()
                .withOwner(owner)
                .withName(categoryDto.name())
                .withDescription(categoryDto.description())
                .withColor(categoryDto.color())
                .build();
        Category result = this.categoryRepository.save(category);
        return mapper.apply(result);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = this.categoryRepository.findById(categoryDto.id()).orElseThrow();
        category.setName(categoryDto.name());
        category.setDescription(categoryDto.description());
        category.setColor(categoryDto.color());
        Category result = this.categoryRepository.save(category);
        return mapper.apply(result);
    }

    @Override
    public void deleteCategory(Long id) {
        this.categoryRepository.deleteById(id);
    }

    @Override
    public Optional<CategoryDto> getCategory(Long id) {
        return this.categoryRepository.findById(id).map(mapper);
    }

    @Override
    public List<CategoryDto> getAllCategories(Long ownerId) {
        return this.categoryRepository.findAllByOwner_Id(ownerId)
                .stream()
                .sorted()
                .map(mapper)
                .toList();
    }
}
