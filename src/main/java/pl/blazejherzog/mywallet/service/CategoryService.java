package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.dto.CategoryDTO;
import pl.blazejherzog.mywallet.model.Budget;
import pl.blazejherzog.mywallet.model.Category;
import pl.blazejherzog.mywallet.repositories.BudgetRepository;
import pl.blazejherzog.mywallet.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/categories")
    public ResponseEntity getCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll()
                .stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/budgets/{budgetId}")
    public ResponseEntity getCategoriesByBudgetId(@PathVariable int budgetId) throws JsonProcessingException {
        List<Category> categoriesByBudget = categoryRepository.findAll().stream()
                .filter(category -> category.getBudget().getBudgetId() == budgetId)
                .collect(Collectors.toList());
        if (categoriesByBudget.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<CategoryDTO> categoryDTOList = categoriesByBudget.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOList);
    }

    @PostMapping("/categories")
    public ResponseEntity addCategory(@RequestBody Category category) {
        Optional<Budget> budgetFromDb = budgetRepository.findById(category.getBudget().getBudgetId());

        if (budgetFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @DeleteMapping("/categories/id/{categoryId}")
    public void deleteCategoryById(@PathVariable int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @DeleteMapping("/categories/name/{categoryName}")
    public void deleteCategoryByName(@PathVariable String categoryName) {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if (category.getCategoryName().equals(categoryName)) {
                categoryRepository.delete(category);
            }
        }
    }

    @DeleteMapping("/categories")
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }


    @PutMapping("/categories/id/{id}")
    public ResponseEntity updateCategory (@PathVariable int id, @RequestBody Category category) throws JsonProcessingException {
        Optional<Budget> userFromDb = budgetRepository.findById(category.getBudget().getBudgetId());
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Optional<Category> categoryFromDb = categoryRepository.findById(id);
        if (categoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        category.setBudget(userFromDb.get());
        category.setId(categoryFromDb.get().getId());
        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(objectMapper.writeValueAsString(updatedCategory));
    }
}
