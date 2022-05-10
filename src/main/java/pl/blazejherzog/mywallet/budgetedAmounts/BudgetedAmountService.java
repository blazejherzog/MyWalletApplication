package pl.blazejherzog.mywallet.budgetedAmounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.budgets.BudgetRepository;
import pl.blazejherzog.mywallet.months.Month;
import pl.blazejherzog.mywallet.months.MonthRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BudgetedAmountService {

    @Autowired
    BudgetedAmountRepository budgetedAmountRepository;

    @Autowired
    MonthRepository monthRepository;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/budgetedamount")
    public ResponseEntity addBudgetedAmount(@RequestBody BudgetedAmount budgetedAmount) throws JsonProcessingException {
        Optional<Month> monthFromDb = monthRepository.findById(budgetedAmount.getMonth().getId());
        if (monthFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        BudgetedAmount savedBudgetedAmount = budgetedAmountRepository.save(budgetedAmount);
        return ResponseEntity.ok(objectMapper.writeValueAsString(savedBudgetedAmount));
    }

    @GetMapping("/budgetedamount/month/{monthId}")
    public ResponseEntity getBudgetedAmountByMonth (@PathVariable String monthId) throws JsonProcessingException {
        Optional<Month> monthFromDb = monthRepository.findById(monthId);
        if (monthFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Optional<BudgetedAmount> budgetedAmountByMonth = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount -> budgetedAmount.getMonth().getId().equals(monthId))
                .findFirst();
        return ResponseEntity.ok(objectMapper.writeValueAsString(budgetedAmountByMonth));
    }

    @GetMapping("/budgetedamount/year/{year}")
    public ResponseEntity getBudgetedAmountsByYear (@PathVariable int year) throws JsonProcessingException {
        List<Month> monthsByYear = monthRepository.findAll().stream()
                .filter(month -> month.getYear() == year)
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(monthsByYear));
    }

    @PutMapping("/budgetedamount/{id}")
    public ResponseEntity updateBudgetedAmount(@PathVariable int id, @RequestBody BudgetedAmount budgetedAmount) throws JsonProcessingException {
        Optional<Month> monthFromDb = monthRepository.findById(budgetedAmount.getMonth().getId());
        if (monthFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Optional<BudgetedAmount> budgetedAmountFromDb = budgetedAmountRepository.findById(budgetedAmount.getId());
        if (budgetedAmountFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        budgetedAmount.setMonth(monthFromDb.get());
        budgetedAmount.setId(budgetedAmountFromDb.get().getId());
        BudgetedAmount saved = budgetedAmountRepository.save(budgetedAmount);
        return ResponseEntity.ok(objectMapper.writeValueAsString(saved));
    }
}
