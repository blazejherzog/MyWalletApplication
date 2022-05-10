package pl.blazejherzog.mywallet.budgetedAmounts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetedAmountRepository extends JpaRepository<BudgetedAmount, Integer> {
}
