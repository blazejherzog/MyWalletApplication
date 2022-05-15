package pl.blazejherzog.mywallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blazejherzog.mywallet.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
}
