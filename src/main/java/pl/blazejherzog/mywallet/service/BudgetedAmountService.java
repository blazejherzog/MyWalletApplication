package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.model.BudgetedAmount;
import pl.blazejherzog.mywallet.model.Category;
import pl.blazejherzog.mywallet.repositories.BudgetedAmountRepository;
import pl.blazejherzog.mywallet.repositories.CategoryRepository;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BudgetedAmountService {

    @Autowired
    BudgetedAmountRepository budgetedAmountRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/budgetedamounts/{budgetedDate}")
    public ResponseEntity getBudgetedAmountsByMonth(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate budgetedDate) throws JsonProcessingException {
        List<BudgetedAmount> budgetsByMonth = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount -> budgetedAmount.getBudgetedDate().getMonth().equals(budgetedDate.getMonth()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(budgetsByMonth));
    }

    @GetMapping("/budgetedamounts")
    public ResponseEntity getBudgetedAmountsByCategory(@RequestParam(value = "categoryName") String categoryName) throws JsonProcessingException {
        List<BudgetedAmount> budgetsByCategory = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount -> budgetedAmount.getCategory().getCategoryName().equals(categoryName))
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(budgetsByCategory));
    }

    @GetMapping("/budgetedamounts/months/{budgetedDate}/{categoryName}")
    public ResponseEntity getBudgetedAmountByMonthAndCategory(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate budgetedDate, @PathVariable String categoryName) throws JsonProcessingException {
        Optional<BudgetedAmount> budgetedAmountByCategoryAndDate = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount -> budgetedAmount.getBudgetedDate().getMonth().equals(budgetedDate.getMonth()))
                .filter(budgetedAmount -> budgetedAmount.getCategory().getCategoryName().equals(categoryName))
                .findFirst();
        if (budgetedAmountByCategoryAndDate.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(budgetedAmountByCategoryAndDate));
    }

    @PostMapping("/budgetedamounts/{categoryName}")
    public ResponseEntity addBudgetedAmount(@PathVariable String categoryName, @RequestBody BudgetedAmount budgetedAmount) throws JsonProcessingException {
        Optional<Category> categoryFromDb = categoryRepository.findAll().stream()
                .filter(category -> category.getCategoryName().equals(categoryName))
                .findFirst();

        if (categoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        BudgetedAmount savedBudgetedAmount = budgetedAmountRepository.save(budgetedAmount);
        return ResponseEntity.ok(objectMapper.writeValueAsString(savedBudgetedAmount));
    }

    @PutMapping("/budgetedamounts/{budgetedDate}/{categoryName}")
    public ResponseEntity updateBudgetedAmount(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate budgetedDate, @PathVariable String categoryName, @RequestBody BudgetedAmount budgetedAmount) throws JsonProcessingException {
        Optional<BudgetedAmount> amountPerCategoryAndMonth = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount1 -> budgetedAmount1.getBudgetedDate().getMonth().equals(budgetedDate.getMonth()))
                .filter(budgetedAmount1 -> budgetedAmount1.getCategory().getCategoryName().equals(categoryName))
                .findFirst();
        if (amountPerCategoryAndMonth.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        budgetedAmount.setBudgetedDate(amountPerCategoryAndMonth.get().getBudgetedDate());
        budgetedAmount.setBudgetedAmount(amountPerCategoryAndMonth.get().getBudgetedAmount());
        budgetedAmount.setCategory(amountPerCategoryAndMonth.get().getCategory());
        BudgetedAmount savedBudgetedAmount = budgetedAmountRepository.save(budgetedAmount);
        return ResponseEntity.ok(objectMapper.writeValueAsString(savedBudgetedAmount));
    }

    @DeleteMapping("/budgetedamounts")
    public void deleteAllBudgetedAmounts() {
        budgetedAmountRepository.deleteAll();
    }

}


