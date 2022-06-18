package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.dto.SubcategoryDTO;
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

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/subcategories")
    public ResponseEntity getSubcategories() {
        List<SubcategoryDTO> subcategories = subcategoryRepository.findAll()
                .stream().map(subcategory -> modelMapper.map(subcategory, SubcategoryDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/subcategories/name/{categoryName}")
    public ResponseEntity getSubcategoriesByCategoryName(@PathVariable String categoryName) {
        List<Subcategory> subcategoriesByCategory = subcategoryRepository.findAll().stream()
                .filter(subcategory -> subcategory.getCategory().getCategoryName().equals(categoryName))
                .collect(Collectors.toList());
        List<SubcategoryDTO> subcategoryDTOList = subcategoriesByCategory.stream().map(subcategory -> modelMapper.map(subcategory, SubcategoryDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(subcategoryDTOList);
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

    @DeleteMapping("/subcategories/id/{subcategoryId}")
    public void deleteSubcategoryById(@PathVariable int subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }

    @DeleteMapping("/subcategories/name/{subcategoryName}")
    public void deleteSubcategoryByName(@PathVariable String subcategoryName) {
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        for (Subcategory subcategory : subcategories) {
            if (subcategory.getName().equals(subcategoryName)) {
                subcategoryRepository.delete(subcategory);
            }
        }
    }

    @PutMapping("/subcategories/id/{id}")
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
