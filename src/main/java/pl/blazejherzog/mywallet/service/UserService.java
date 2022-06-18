package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.dto.UserDTO;
import pl.blazejherzog.mywallet.model.User;
import pl.blazejherzog.mywallet.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;


    @GetMapping("/users")
    public ResponseEntity getUsers() {
        List<UserDTO> users = userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/nickname/{nickName}")
    public ResponseEntity getUserByNickName (@PathVariable String nickName)  {
        Optional<UserDTO> userFromDb = userRepository.findByNickName(nickName)
                .stream().map(user -> modelMapper.map(user, UserDTO.class))
                .findFirst();
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userFromDb);
    }

    @GetMapping("/users/email/{userEmail}")
    public ResponseEntity getUserByEmail (@PathVariable String userEmail) {
        Optional<UserDTO> userFromDb = userRepository.findByUserEmail(userEmail)
                .stream().map(user -> modelMapper.map(user, UserDTO.class))
                .findFirst();
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userFromDb);
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) throws JsonProcessingException {

        Optional<User> byUserEmail = userRepository.findByUserEmail(user.getUserEmail());
        Optional<User> byUserNickName = userRepository.findByNickName(user.getNickName());

        if (byUserEmail.isPresent() || byUserNickName.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(objectMapper.writeValueAsString(savedUser));
    }

    @DeleteMapping("/users")
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }


    @DeleteMapping("/users/email/{userEmail}")
    public void deleteUserByEmail(@PathVariable String userEmail) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUserEmail().equals(userEmail)) {
                userRepository.delete(user);
            }
        }
    }

    @DeleteMapping("/users/id/{userId}")
    public void deleteUserById(@PathVariable int userId) {
        userRepository.deleteById(userId);
    }

    @PutMapping("/users/id/{userId}")
    public ResponseEntity updateUser(@PathVariable int userId, @RequestBody User user) throws JsonProcessingException {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        user.setUserId(userFromDb.get().getUserId());
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(objectMapper.writeValueAsString(savedUser));
    }
}
