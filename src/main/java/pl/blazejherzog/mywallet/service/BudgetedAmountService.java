package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.dto.BudgetedAmountDTO;
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

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/budgetedamounts")
    public ResponseEntity getAllBudgetedAmounts() {
        List<BudgetedAmountDTO> allBudgeted = budgetedAmountRepository.findAll()
                .stream().map(budgetedAmount -> modelMapper.map(budgetedAmount, BudgetedAmountDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(allBudgeted);
    }

    @GetMapping("/budgetedamounts/months/{budgetedDate}")
    public ResponseEntity getBudgetedAmountsByMonth(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate budgetedDate) {
        List<BudgetedAmount> budgetsByMonth = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount -> budgetedAmount.getBudgetedDate().getMonth().equals(budgetedDate.getMonth()))
                .collect(Collectors.toList());
        List<BudgetedAmountDTO> budgetedAmountDTOList = budgetsByMonth.stream().map(budgetedAmount -> modelMapper.map(budgetedAmount, BudgetedAmountDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(budgetedAmountDTOList);
    }

    @GetMapping("/budgetedamounts/categoryName/{categoryName}")
    public ResponseEntity getBudgetedAmountsByCategory(String categoryName) {
        List<BudgetedAmount> budgetsByCategory = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount -> budgetedAmount.getCategory().getCategoryName().equals(categoryName))
                .collect(Collectors.toList());
        List<BudgetedAmountDTO> budgetedAmountDTOList = budgetsByCategory.stream().map(budgetedAmount -> modelMapper.map(budgetedAmount, BudgetedAmountDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(budgetedAmountDTOList);
    }

    @GetMapping("/budgetedamounts/months/{budgetedDate}/categories/{categoryName}")
    public ResponseEntity getBudgetedAmountByMonthAndCategory(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate budgetedDate, @PathVariable String categoryName) {
        Optional<BudgetedAmount> budgetedAmountByCategoryAndDate = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount -> budgetedAmount.getBudgetedDate().getMonth().equals(budgetedDate.getMonth()))
                .filter(budgetedAmount -> budgetedAmount.getCategory().getCategoryName().equals(categoryName))
                .findFirst();
        if (budgetedAmountByCategoryAndDate.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<BudgetedAmountDTO> budgetedAmountDTOList = budgetedAmountByCategoryAndDate.stream().map(
                        budgetedAmount -> modelMapper.map(budgetedAmount, BudgetedAmountDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(budgetedAmountDTOList);
    }

    @PostMapping("/budgetedamounts")
    public ResponseEntity addBudgetedAmount(@RequestBody BudgetedAmount budgetedAmount) throws JsonProcessingException {
        Optional<Category> categoryFromDb = categoryRepository.findById(budgetedAmount.getCategory().getId());

        if (categoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        BudgetedAmount savedBudgetedAmount = budgetedAmountRepository.save(budgetedAmount);
        return ResponseEntity.ok(objectMapper.writeValueAsString(savedBudgetedAmount));
    }

    @PutMapping("/budgetedamounts/months/{budgetedDate}/categories/{categoryName}")
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

    @DeleteMapping("/budgetedamounts/id/{id}")
    public void deleteBudgetedAmountsById(@PathVariable int id) {
        budgetedAmountRepository.deleteById(id);
    }
}


