package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.dto.BudgetDTO;
import pl.blazejherzog.mywallet.model.Budget;
import pl.blazejherzog.mywallet.repositories.BudgetRepository;
import pl.blazejherzog.mywallet.model.User;
import pl.blazejherzog.mywallet.repositories.CategoryRepository;
import pl.blazejherzog.mywallet.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper  modelMapper;

    @GetMapping("/budgets")
    public ResponseEntity getBudgets() {
        List<BudgetDTO> allBudgets = budgetRepository.findAll()
                .stream().map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(allBudgets);
    }

    @GetMapping("/budgets/users/{userId}")
    public ResponseEntity getUsersBudget(@PathVariable int userId) throws JsonProcessingException {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Optional<Budget> budgetByUser = budgetRepository.findAll().stream()
                .filter(budget -> budget.getUser().getUserId() == userId)
                .findFirst();
        Optional<BudgetDTO> response = budgetByUser.stream().map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .findFirst();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/budgets")
    public ResponseEntity addBudget (@RequestBody Budget budget) {
        Optional<User> userFromDb = userRepository.findById(budget.getUser().getUserId());
        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        budget.setUser(userFromDb.get());
        Budget savedBudget = budgetRepository.save(budget);
        return ResponseEntity.ok(savedBudget);
    }

    @PutMapping("/budgets/id/{budgetId}")
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

    @DeleteMapping("/budgets/id/{budgetId}")
    public void deleteBudgetById (@PathVariable int budgetId) {
        budgetRepository.deleteById(budgetId);
    }

}
