package pl.blazejherzog.mywallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blazejherzog.mywallet.model.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {
}
