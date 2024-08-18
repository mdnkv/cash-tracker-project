package dev.mednikov.cashtracker.categories.services;

import com.github.javafaker.Faker;

import dev.mednikov.cashtracker.categories.dto.CategoryDto;
import dev.mednikov.cashtracker.categories.models.Category;
import dev.mednikov.cashtracker.categories.repositories.CategoryRepository;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private final static Faker faker = new Faker();

    @Mock private CategoryRepository categoryRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private CategoryServiceImpl categoryService;

    @Test
    void createCategoryTest(){
        Long ownerId = faker.random().nextLong();
        User owner = new User.UserBuilder().withId(ownerId).withEmail(faker.internet().emailAddress()).build();

        Category category = new Category.CategoryBuilder()
                .withId(faker.random().nextLong())
                .withOwner(owner)
                .withName(faker.lorem().fixedString(25))
                .withDescription(faker.lorem().fixedString(150))
                .withColor(faker.color().hex())
                .build();

        CategoryDto categoryDto = new CategoryDto(null, ownerId, category.getName(), category.getDescription(), category.getColor());

        Mockito.when(userRepository.getReferenceById(ownerId)).thenReturn(owner);
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto result = categoryService.createCategory(categoryDto);
        Assertions
                .assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", category.getId())
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrPropertyWithValue("description", category.getDescription())
                .hasFieldOrPropertyWithValue("color", category.getColor())
                .hasFieldOrPropertyWithValue("ownerId", ownerId);

    }

    @Test
    void updateCategoryTest(){
        User owner = new User.UserBuilder()
                .withId(faker.random().nextLong())
                .withEmail(faker.internet().emailAddress())
                .build();
        Long categoryId = faker.random().nextLong();
        Category category = new Category.CategoryBuilder()
                .withId(categoryId)
                .withOwner(owner)
                .withName(faker.lorem().fixedString(25))
                .withDescription(faker.lorem().fixedString(150))
                .withColor(faker.color().hex())
                .build();

        CategoryDto categoryDto = new CategoryDto(categoryId, owner.getId(), category.getName(),
                category.getDescription(), category.getColor());

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto result = categoryService.updateCategory(categoryDto);
        Assertions.assertThat(result)
                .hasFieldOrPropertyWithValue("id", categoryId)
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrPropertyWithValue("description", category.getDescription())
                .hasFieldOrPropertyWithValue("color", category.getColor());
    }

    @Test
    void getCategoryById_existsTest(){
        User owner = new User.UserBuilder()
                .withId(faker.random().nextLong())
                .withEmail(faker.internet().emailAddress())
                .build();
        Long categoryId = faker.random().nextLong();
        Category category = new Category.CategoryBuilder()
                .withId(categoryId)
                .withOwner(owner)
                .withName(faker.lorem().fixedString(25))
                .withDescription(faker.lorem().fixedString(150))
                .withColor(faker.color().hex())
                .build();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Optional<CategoryDto> result = categoryService.getCategory(categoryId);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void getCategoryById_notExistsTest(){
        Long categoryId = faker.random().nextLong();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Optional<CategoryDto> result = categoryService.getCategory(categoryId);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getAllCategoriesTest(){
        Long ownerId = faker.random().nextLong();
        User owner = new User.UserBuilder()
                .withId(ownerId)
                .withEmail(faker.internet().emailAddress())
                .build();

        List<Category> categories = List.of(
                new Category.CategoryBuilder()
                        .withId(faker.random().nextLong())
                        .withOwner(owner)
                        .withName(faker.lorem().fixedString(25))
                        .withDescription(faker.lorem().fixedString(150))
                        .withColor(faker.color().hex())
                        .build(),
                new Category.CategoryBuilder()
                        .withId(faker.random().nextLong())
                        .withOwner(owner)
                        .withName(faker.lorem().fixedString(25))
                        .withDescription(faker.lorem().fixedString(150))
                        .withColor(faker.color().hex())
                        .build(),
                new Category.CategoryBuilder()
                        .withId(faker.random().nextLong())
                        .withOwner(owner)
                        .withName(faker.lorem().fixedString(25))
                        .withDescription(faker.lorem().fixedString(150))
                        .withColor(faker.color().hex())
                        .build(),
                new Category.CategoryBuilder()
                        .withId(faker.random().nextLong())
                        .withOwner(owner)
                        .withName(faker.lorem().fixedString(25))
                        .withDescription(faker.lorem().fixedString(150))
                        .withColor(faker.color().hex())
                        .build(),
                new Category.CategoryBuilder()
                        .withId(faker.random().nextLong())
                        .withOwner(owner)
                        .withName(faker.lorem().fixedString(25))
                        .withDescription(faker.lorem().fixedString(150))
                        .withColor(faker.color().hex())
                        .build()
        );

        Mockito.when(categoryRepository.findAllByOwner_Id(ownerId)).thenReturn(categories);

        List<CategoryDto> result = categoryService.getAllCategories(ownerId);
        Assertions.assertThat(result).hasSize(categories.size());

    }

}
