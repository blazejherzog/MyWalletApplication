package pl.blazejherzog.mywallet.transactions;

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
import java.util.stream.Collectors;

@RestController
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping("/transactions")
    public ResponseEntity addTransaction (@RequestHeader User user, @RequestBody Transaction transaction) {
        Optional<User> userFromDb = userRepository.findByUserEmail(user.getUserEmail());

        if (userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Transaction addedTransaction = new Transaction(transaction.getId(), transaction.getName(), transaction.getDate(), transaction.getAmount(), transaction.getSubcategory(), transaction.getBudget(), userFromDb.get());
        Transaction savedTransaction = transactionRepository.save(addedTransaction);

        return ResponseEntity.ok(savedTransaction);
    }

    @GetMapping("/transactions")
    public ResponseEntity getTransactions() throws JsonProcessingException {
        List<Transaction> transactions = transactionRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(transactions));
    }

    @DeleteMapping("/transactions")
    public ResponseEntity deleteTransactionById(int transactionId) throws JsonProcessingException {
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getId() != transactionId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(transactions));
    }

    @DeleteMapping("/transactions")
    public ResponseEntity deleteTransactionByName(String transactionName) throws JsonProcessingException {
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getName() != transactionName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(objectMapper.writeValueAsString(transactions));
    }

}
