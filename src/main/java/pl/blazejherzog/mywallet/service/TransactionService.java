package pl.blazejherzog.mywallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.model.Budget;
import pl.blazejherzog.mywallet.model.Subcategory;
import pl.blazejherzog.mywallet.model.Transaction;
import pl.blazejherzog.mywallet.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/budgets/{budgetId}/transactions")
    public ResponseEntity addTransaction (@RequestBody Transaction transaction, @PathVariable int budgetId, Budget budget) {
        Optional<Budget> budgetFromDb = budgetRepository.findAll().stream()
                .filter(budget1 -> budget1.getBudgetId() == budgetId)
                .findFirst();

        if (budgetFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Transaction addedTransaction = transactionRepository.save(transaction);
        budgetFromDb.get().setAmount(budgetFromDb.get().getAmount() - addedTransaction.getAmount());
        budgetRepository.flush();

        return ResponseEntity.ok(addedTransaction);
    }

    @GetMapping("/transactions")
    public ResponseEntity getTransactions() throws JsonProcessingException {
        List<Transaction> transactions = transactionRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(transactions));
    }


    @GetMapping("/transactions/id/{id}")
    public ResponseEntity getTransactionById(@PathVariable int id) throws JsonProcessingException {
        Optional<Transaction> filteredTransaction = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getId() == id)
                .findFirst();
        return ResponseEntity.ok(objectMapper.writeValueAsString(filteredTransaction));
    }


    @GetMapping("/transactions/name/{name}")
    public ResponseEntity getTransactionsByName(@PathVariable String name) throws JsonProcessingException {
        List<Transaction> filteredTransactions = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getName().equals(name))
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(filteredTransactions));
    }

    @GetMapping("/transactions/budget/{budgetId}")
    public ResponseEntity getTransactionsByBudget(@PathVariable int budgetId) throws JsonProcessingException {
        Optional<Budget> budgetFromDb = budgetRepository.findAll().stream()
                .filter(budget -> budget.getBudgetId() == budgetId)
                .findFirst();
        if (budgetFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        List<Transaction> transactionsByBudget = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getBudget().getBudgetId() == budgetId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(transactionsByBudget));
    }

    @GetMapping("/transactions/budget/{budgetId}/month/{transactionDate}")
    public ResponseEntity getTransactionsByBudgetAndMonth(@PathVariable int budgetId, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate transactionDate) throws JsonProcessingException {
        Optional<Budget> budgetFromDb = budgetRepository.findAll().stream()
                .filter(budget -> budget.getBudgetId() == budgetId)
                .findFirst();
        if (budgetFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        List<Transaction> transactionsByBudgetAndMonth = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getBudget().getBudgetId() == budgetId)
                .filter(transaction -> transaction.getDate().getMonth().equals(transactionDate.getMonth()))
                .collect(Collectors.toList());
        if (transactionsByBudgetAndMonth.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(transactionsByBudgetAndMonth));
    }

    @DeleteMapping("/budgets/{budgetId}/transactions/{transactionId}")
    public void deleteTransactionById(@PathVariable int budgetId, @PathVariable int transactionId) {
        Optional<Budget> budgetFromDb = budgetRepository.findAll().stream()
                .filter(budget -> budget.getBudgetId() == budgetId)
                .findFirst();
        Optional<Transaction> transactionFromDb = transactionRepository.findById(transactionId);
        int amount = transactionFromDb.get().getAmount();
        transactionRepository.deleteById(transactionId);
        budgetFromDb.get().setAmount(budgetFromDb.get().getAmount() + amount);
        budgetRepository.flush();
    }

    @DeleteMapping("/transactions/name/{name}")
    public void deleteTransactionByName(@PathVariable String name) {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            if (transaction.getName().equals(name)) {
                transactionRepository.delete(transaction);
            }
        }
    }

    @DeleteMapping("/transactions/all")
    public void deleteAllTransactions() {
        transactionRepository.deleteAll();
    }

    @PutMapping("/budgets/{budgetId}/transactions/id/{transactionId}")
    public ResponseEntity updateTransaction(@PathVariable int transactionId, @PathVariable int budgetId, @RequestBody Transaction transaction) throws JsonProcessingException {
        Optional<Budget> budgetFromDb = budgetRepository.findAll().stream()
                .filter(budget1 -> budget1.getBudgetId() == budgetId)
                .findFirst();
        if (budgetFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Subcategory> subcategoryFromDb = subcategoryRepository.findById(transaction.getSubcategory().getId());
        if (subcategoryFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Transaction> transactionFromDb = transactionRepository.findById(transactionId);
        if (transactionFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        int oldAmount = transactionFromDb.get().getAmount();
        transaction.setBudget(budgetFromDb.get());
        transaction.setSubcategory(subcategoryFromDb.get());
        transaction.setId(transactionFromDb.get().getId());
        Transaction updatedTransaction = transactionRepository.save(transaction);

        int updatedAmount = updatedTransaction.getAmount();
        int changeInBudget = updatedAmount - oldAmount;
        budgetFromDb.get().setAmount(budgetFromDb.get().getAmount() - changeInBudget);
        budgetRepository.flush();
        return ResponseEntity.ok(objectMapper.writeValueAsString(updatedTransaction));
    }
}
