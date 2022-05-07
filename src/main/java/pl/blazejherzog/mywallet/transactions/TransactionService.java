package pl.blazejherzog.mywallet.transactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.blazejherzog.mywallet.Budget;
import pl.blazejherzog.mywallet.categories.CategoryRepository;
import pl.blazejherzog.mywallet.subcategories.Subcategory;
import pl.blazejherzog.mywallet.users.User;
import pl.blazejherzog.mywallet.users.UserRepository;

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
    ObjectMapper objectMapper;

    @PostMapping("/transactions")
    public ResponseEntity addTransaction (@RequestBody Transaction transaction) {
        Optional<User> userFromDb = userRepository.findById(transaction.getUser().getUserId());

        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Transaction addedTransaction = transactionRepository.save(transaction);

        return ResponseEntity.ok(addedTransaction);
    }

    @GetMapping("/transactions")
    public ResponseEntity getTransactions() throws JsonProcessingException {
        List<Transaction> transactions = transactionRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(transactions));
    }


    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity getTransactionById(@PathVariable int transactionId) throws JsonProcessingException {
        List<Transaction> filteredTransactions = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getId() == transactionId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(filteredTransactions));
    }


    @GetMapping("transactions/{name}")
    public ResponseEntity getTransactionsByName(@PathVariable String name) throws JsonProcessingException {
        List<Transaction> filteredTransactions = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getName().equals(name))
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(filteredTransactions));
    }

    @DeleteMapping("/transactions/{transactionId}")
    public void deleteTransactionById(@PathVariable int transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    @DeleteMapping("/transactions{transactionName}")
    public void deleteTransactionByName(@PathVariable String transactionName) {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            if (transaction.getName().equals(transactionName)) {
                transactionRepository.delete(transaction);
            }
        }
    }

    @PutMapping("/transactions")
    public ResponseEntity updateTransaction (int id, String name, LocalDate date, int amount, Subcategory subcategory, Budget budget, User user) {
        Transaction updatedTransaction = Transaction.builder()
                .id(id)
                .name(name)
                .date(date)
                .amount(amount)
                .subcategory(subcategory)
                .budget(budget)
                .user(user)
                .build();
        Optional<Transaction> transaction = transactionRepository.findById(id).map(savedTransaction -> transactionRepository.save(updatedTransaction));
        return ResponseEntity.ok(transaction);
    }

}
