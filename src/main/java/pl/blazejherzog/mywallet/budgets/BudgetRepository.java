package pl.blazejherzog.mywallet.budgets;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blazejherzog.mywallet.budgets.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
}
