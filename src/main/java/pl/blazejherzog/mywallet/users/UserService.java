package pl.blazejherzog.mywallet.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/users")
    public ResponseEntity getUsers() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(users));
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) throws JsonProcessingException {

        Optional<User> userFromDb = userRepository.findByUserEmail(user.getUserEmail());

        if (userFromDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(objectMapper.writeValueAsString(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        Optional<User> userFromDb = userRepository.findByUserEmail(user.getUserEmail());
        if (userFromDb.isEmpty() || wrongPassword(userFromDb, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    private boolean wrongPassword(Optional<User> userFromDb, User user) {
        return !userFromDb.get().getPassword().equals(user.getPassword());
    }

    @DeleteMapping("/users")
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }


    @DeleteMapping("/users/{userEmail}")
    public void deleteUserByEmail(@PathVariable String userEmail) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUserEmail().equals(userEmail)) {
                userRepository.delete(user);
            }
        }
    }

    @DeleteMapping("users/{userId}")
    public void deleteUserById(@PathVariable int userId) {
        userRepository.deleteById(userId);
    }
}
