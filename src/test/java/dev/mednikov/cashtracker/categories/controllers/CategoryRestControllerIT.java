package dev.mednikov.cashtracker.categories.controllers;

import dev.mednikov.cashtracker.categories.dto.CategoryDto;
import dev.mednikov.cashtracker.categories.models.Category;
import dev.mednikov.cashtracker.categories.repositories.CategoryRepository;
import dev.mednikov.cashtracker.core.BaseIT;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;
import dev.mednikov.cashtracker.users.services.JwtService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.Optional;

class CategoryRestControllerIT extends BaseIT {

    @Autowired private JwtService jwtService;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;

    @Test
    void createCategoryTest() {
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        // prepare payload
        CategoryDto payload = new CategoryDto(
                null,
                user.getId(),
                faker.lorem().fixedString(25),
                faker.lorem().fixedString(100),
                faker.color().hex()
        );

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<CategoryDto> request = new HttpEntity<>(payload, headers);

        // execute request
        ResponseEntity<CategoryDto> response = testRestTemplate.exchange(
                "/api/categories/create", HttpMethod.POST, request, CategoryDto.class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();


    }

    @Test
    void updateCategoryTest() {
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        // create existing category
        Category category = new Category.CategoryBuilder()
                .withOwner(userResult)
                .withName(faker.lorem().fixedString(30))
                .withDescription(faker.lorem().fixedString(90))
                .withColor(faker.color().hex())
                .build();
        Category categoryResult = categoryRepository.save(category);

        // prepare payload
        CategoryDto payload = new CategoryDto(
                categoryResult.getId(),
                user.getId(),
                faker.lorem().fixedString(25),
                faker.lorem().fixedString(100),
                faker.color().hex()
        );

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<CategoryDto> request = new HttpEntity<>(payload, headers);

        // execute request
        ResponseEntity<CategoryDto> response = testRestTemplate.exchange(
                "/api/categories/update", HttpMethod.PUT, request, CategoryDto.class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        // assert that category was updated in datasource
        Category result = categoryRepository.findById(categoryResult.getId()).orElseThrow();
        Assertions
                .assertThat(result)
                .hasFieldOrPropertyWithValue("name", payload.name())
                .hasFieldOrPropertyWithValue("description", payload.description())
                .hasFieldOrPropertyWithValue("color", payload.color());

    }

    @Test
    void deleteCategoryTest() {
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        // create existing category
        Category category = new Category.CategoryBuilder()
                .withOwner(userResult)
                .withName(faker.lorem().fixedString(30))
                .withDescription(faker.lorem().fixedString(90))
                .withColor(faker.color().hex())
                .build();
        Category categoryResult = categoryRepository.save(category);
        Long categoryId = categoryResult.getId();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<CategoryDto> response = testRestTemplate.exchange(
                "/api/categories/delete/{id}", HttpMethod.DELETE, request, CategoryDto.class, categoryId
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // assert that category was deleted
        Optional<Category> result = categoryRepository.findById(categoryResult.getId());
        Assertions.assertThat(result).isEmpty();

    }

    @Test
    void getCategoryById_existsTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        // create existing category
        Category category = new Category.CategoryBuilder()
                .withOwner(userResult)
                .withName(faker.lorem().fixedString(30))
                .withDescription(faker.lorem().fixedString(90))
                .withColor(faker.color().hex())
                .build();
        Category categoryResult = categoryRepository.save(category);
        Long categoryId = categoryResult.getId();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<CategoryDto> response = testRestTemplate.exchange(
                "/api/categories/one/{id}", HttpMethod.GET, request, CategoryDto.class, categoryId
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

    }

    @Test
    void getCategoryById_notExistsTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        Long categoryId = faker.random().nextLong();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<CategoryDto> response = testRestTemplate.exchange(
                "/api/categories/one/{id}", HttpMethod.GET, request, CategoryDto.class, categoryId
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void getAllCategoriesTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<CategoryDto[]> response = testRestTemplate.exchange(
                "/api/categories/list", HttpMethod.GET, request, CategoryDto[].class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
