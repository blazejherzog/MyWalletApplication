package pl.blazejherzog.mywallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.blazejherzog.mywallet.model.BudgetedAmount;
import pl.blazejherzog.mywallet.model.Transaction;
import pl.blazejherzog.mywallet.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ApplicationController {

    @Autowired
    BudgetedAmountRepository budgetedAmountRepository;

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/month/{transactionsDate}/expenses")
    public ResponseEntity getAllMonthlyExpenses(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate transactionsDate) {
        List<Transaction> allTransactionsByMonth = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getDate().getMonth().equals(transactionsDate.getMonth()))
                .collect(Collectors.toList());
        int totalAmount = 0;
        for (Transaction transaction : allTransactionsByMonth) {
            int amount = transaction.getAmount();
            totalAmount += amount;

        }
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/month/{transactionsDate}/categories/{categoryName}/expenses")
    public ResponseEntity getMonthlyExpensesByCategory(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate transactionsDate, @PathVariable String categoryName) {
        List<Transaction> allTransactionsByMonthAndCategory = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getDate().getMonth().equals(transactionsDate.getMonth()))
                .filter(transaction -> transaction.getSubcategory().getCategory().getCategoryName().equals(categoryName))
                .collect(Collectors.toList());
        int totalAmount = 0;
        for (Transaction transaction : allTransactionsByMonthAndCategory) {
            int amount = transaction.getAmount();
            totalAmount += amount;
        }
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/month/{transactionsDate}/categories/{categoryName}/budgetedamounts/left")
    public ResponseEntity getAmountStillToSpend(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate transactionsDate, @PathVariable String categoryName) {
        List<Transaction> allTransactionsByMonthAndCategory = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getDate().getMonth().equals(transactionsDate.getMonth()))
                .filter(transaction -> transaction.getSubcategory().getCategory().getCategoryName().equals(categoryName))
                .collect(Collectors.toList());
        int totalAmount = 0;
        for (Transaction transaction : allTransactionsByMonthAndCategory) {
            int amount = transaction.getAmount();
            totalAmount += amount;
        }
        Optional<BudgetedAmount> monthlyBudgetedAmountFromDb = budgetedAmountRepository.findAll().stream()
                .filter(budgetedAmount -> budgetedAmount.getBudgetedDate().getMonth().equals(transactionsDate.getMonth()))
                .filter(budgetedAmount -> budgetedAmount.getCategory().getCategoryName().equals(categoryName))
                .findFirst();

        int amountStillToSpend = monthlyBudgetedAmountFromDb.get().getBudgetedAmount() - totalAmount;
        return ResponseEntity.ok(amountStillToSpend);
    }
}
