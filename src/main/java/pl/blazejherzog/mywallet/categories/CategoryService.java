package pl.blazejherzog.mywallet.categories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.users.User;
import pl.blazejherzog.mywallet.users.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/categories")
    public ResponseEntity getCategories() throws JsonProcessingException {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(categories));
    }

    @PostMapping("/categories")
    public ResponseEntity addCategory(@RequestHeader String userEmail, @RequestBody Category category) {
        Optional<User> userFromDb = userRepository.findByUserEmail(userEmail);

        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Category addedCategory = new Category(category.getId(), category.getCategoryName(), category.getUser(), category.getSubcategoryList());
        Category savedCategory = categoryRepository.save(addedCategory);
        return ResponseEntity.ok(savedCategory);
    }

    @DeleteMapping("/categories{categoryId}")
    public void deleteCategoryById(int categoryId) {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                categoryRepository.deleteById(categoryId);
            }
        }
    }

    @DeleteMapping("/categories{categoryName}")
    public void deleteCategoryByName(String categoryName) {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if (category.getCategoryName().equals(categoryName)) {
                categoryRepository.delete(category);
            }
        }
    }

    @PutMapping("/categories")
    public ResponseEntity updateCategory (int id, String name, User user) {
        Category updatedCategory = Category.builder()
                .id(id)
                .categoryName(name)
                .user(user)
                .build();
        Optional<Category> category = categoryRepository.findById(id)
                .map(savedCategory -> categoryRepository.save(updatedCategory));
        return ResponseEntity.ok(category);

    }
}
