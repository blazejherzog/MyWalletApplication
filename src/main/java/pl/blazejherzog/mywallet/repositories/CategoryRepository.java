package pl.blazejherzog.mywallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blazejherzog.mywallet.model.Category;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryName(String name);
}
