package pl.blazejherzog.mywallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blazejherzog.mywallet.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
