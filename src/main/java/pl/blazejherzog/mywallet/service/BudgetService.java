package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.model.Budget;
import pl.blazejherzog.mywallet.repositories.BudgetRepository;
import pl.blazejherzog.mywallet.model.User;
import pl.blazejherzog.mywallet.repositories.UserRepository;

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

    @GetMapping("/budget/{userId}")
    public ResponseEntity getUsersBudget(@PathVariable int userId) throws JsonProcessingException {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Optional<Budget> budgetByUser = budgetRepository.findAll().stream()
                .filter(budget -> budget.getUser().getUserId() == userId)
                .findFirst();
        return ResponseEntity.ok(objectMapper.writeValueAsString(budgetByUser));
    }

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

    @PutMapping("/budget/{budgetId}")
    public ResponseEntity updateBudget (@PathVariable int budgetId, @RequestBody Budget budget) throws JsonProcessingException {
        Optional<User> userFromDb = userRepository.findById(budget.getUser().getUserId());
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Optional<Budget> budgetFromDb = budgetRepository.findById(budgetId);
        if (budgetFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        budget.setUser(userFromDb.get());
        budget.setBudgetId(budgetFromDb.get().getBudgetId());
        Budget updatedBudget = budgetRepository.save(budget);
        return ResponseEntity.ok(objectMapper.writeValueAsString(updatedBudget));
    }

    @DeleteMapping("/budget/{budgetId}")
    public void deleteBudgetById (@PathVariable int budgetId) {
        budgetRepository.deleteById(budgetId);
    }
}
