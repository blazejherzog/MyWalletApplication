package pl.blazejherzog.mywallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blazejherzog.mywallet.model.BudgetedAmount;

public interface BudgetedAmountRepository extends JpaRepository<BudgetedAmount, Integer> {
}
