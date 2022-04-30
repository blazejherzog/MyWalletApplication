package pl.blazejherzog.mywallet;

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
        Category addedCategory = new Category(category.getId(), category.getName(), category.getUser());
        Category savedCategory = categoryRepository.save(addedCategory);
        return ResponseEntity.ok(savedCategory);
    }
}
