package dev.mednikov.cashtracker.categories.controllers;

import dev.mednikov.cashtracker.categories.dto.CategoryDto;
import dev.mednikov.cashtracker.categories.services.CategoryService;

import dev.mednikov.cashtracker.users.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto){
        return this.categoryService.createCategory(categoryDto);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto){
        return this.categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory (@PathVariable Long id){
        this.categoryService.deleteCategory(id);
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id){
        Optional<CategoryDto> result = this.categoryService.getCategory(id);
        return ResponseEntity.of(result);
    }

    @GetMapping("/list")
    public @ResponseBody List<CategoryDto> getAllCategories(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return this.categoryService.getAllCategories(user.getId());
    }

}
