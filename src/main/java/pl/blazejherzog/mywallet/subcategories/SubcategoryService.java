package pl.blazejherzog.mywallet.subcategories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.categories.Category;
import pl.blazejherzog.mywallet.categories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    @PostMapping("/subcategories")
    public ResponseEntity addSubcategory(@RequestHeader String categoryName, @RequestBody Subcategory subcategory) {
        Optional<Category> categoryFromDb = categoryRepository.findByCategoryName(categoryName);
        if (categoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Subcategory addedSubcategory = new Subcategory(subcategory.getId(), subcategory.getName(), subcategory.getCategory());
        Subcategory savedSubcategory = subcategoryRepository.save(addedSubcategory);
        return ResponseEntity.ok(savedSubcategory);
    }

    @DeleteMapping("/subcategories{subcategoryId}")
    public void deleteSubcategoryById(int subcategoryId) {
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        for (Subcategory subcategory : subcategories) {
            if (subcategory.getId() == subcategoryId) {
                subcategoryRepository.delete(subcategory);
            }
        }
    }

    @DeleteMapping("/subcategories{subcategoryName}")
    public void deleteSubcategoryByName(String subcategoryName) {
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        for (Subcategory subcategory : subcategories) {
            if (subcategory.getName().equals(subcategoryName)) {
                subcategoryRepository.delete(subcategory);
            }
        }
    }

    @PutMapping("/subcategories")
    public ResponseEntity updateSubcategory(int id, String name, Category category) {
        Subcategory updatedSubcategory = Subcategory.builder()
                .id(id)
                .name(name)
                .category(category)
                .build();
        Optional<Subcategory> subcategory = subcategoryRepository.findById(id)
                .map(savedSubcategory -> subcategoryRepository.save(updatedSubcategory));
        return ResponseEntity.ok(subcategory);
    }
}
