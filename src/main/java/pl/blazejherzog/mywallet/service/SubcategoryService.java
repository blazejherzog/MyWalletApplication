package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.model.Category;
import pl.blazejherzog.mywallet.model.Subcategory;
import pl.blazejherzog.mywallet.repositories.CategoryRepository;
import pl.blazejherzog.mywallet.repositories.SubcategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SubcategoryService {

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/subcategories")
    public ResponseEntity getSubcategories() throws JsonProcessingException {
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(subcategories));
    }

    @GetMapping("/subcategories/{categoryName}")
    public ResponseEntity getSubcategoriesByCategoryName(@PathVariable String categoryName) throws JsonProcessingException {
        Optional<Category> categoryFromDb = categoryRepository.findByCategoryName(categoryName);
        if (categoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        List<Subcategory> subcategoriesByCategory = subcategoryRepository.findAll().stream()
                .filter(subcategory -> subcategory.getCategory().getCategoryName().equals(categoryName))
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(subcategoriesByCategory));
    }

    @PostMapping("/subcategories")
    public ResponseEntity addSubcategory(@RequestBody Subcategory subcategory) {
        Optional<Category> categoryFromDb = categoryRepository.findById(subcategory.getCategory().getId());
        if (categoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Subcategory savedSubcategory = subcategoryRepository.save(subcategory);
        return ResponseEntity.ok(savedSubcategory);
    }

    @DeleteMapping("/subcategories/{subcategoryId}")
    public void deleteSubcategoryById(@PathVariable int subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }

    @DeleteMapping("/subcategories/{subcategoryName}")
    public void deleteSubcategoryByName(@RequestParam(value = "subcategoryName") String subcategoryName) {
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        for (Subcategory subcategory : subcategories) {
            if (subcategory.getName().equals(subcategoryName)) {
                subcategoryRepository.delete(subcategory);
            }
        }
    }

    @PutMapping("/subcategories/{id}")
    public ResponseEntity updateSubcategory (@PathVariable int id, @RequestBody Subcategory subcategory) throws JsonProcessingException {
        Optional<Category> categoryFromDb = categoryRepository.findById(subcategory.getCategory().getId());
        if (categoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
        Optional<Subcategory> subcategoryFromDb = subcategoryRepository.findById(id);
        if (subcategoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        subcategory.setCategory(categoryFromDb.get());
        subcategory.setId(subcategoryFromDb.get().getId());
        Subcategory updatedSubcategory = subcategoryRepository.save(subcategory);
        return ResponseEntity.ok(objectMapper.writeValueAsString(updatedSubcategory));
    }

}
