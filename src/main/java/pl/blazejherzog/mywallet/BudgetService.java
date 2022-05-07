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
public class BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/budgets")
    public ResponseEntity getBudgets() throws JsonProcessingException {
        List<Budget> allBudgets = budgetRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(allBudgets));
    }

//    @GetMapping("/budget/userId")
//    public ResponseEntity getUsersBudget(@PathVariable int userId) throws JsonProcessingException {
//        Optional<User> userFromDb = userRepository.findAll().stream()
//                .filter(user -> user.getUserId() == userId)
//                .findFirst();
//        if (userFromDb.isPresent()) {
//            Budget userBudget = userFromDb.get().getBudget();
//
//            return ResponseEntity.ok(objectMapper.writeValueAsString(userBudget));
//        }
//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//    }

    @PostMapping("/budget")
    public ResponseEntity addBudget (@RequestBody Budget budget) {
        Optional<User> userFromDb = userRepository.findById(budget.getUser().getUserId());
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        budget.setUser(userFromDb.get());
        Budget savedBudget = budgetRepository.save(budget);
        return ResponseEntity.ok(savedBudget);
    }
}
