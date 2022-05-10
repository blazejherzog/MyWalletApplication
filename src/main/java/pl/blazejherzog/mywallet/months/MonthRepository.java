package pl.blazejherzog.mywallet.months;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthRepository extends JpaRepository<Month, Integer> {
    Optional<Month> findById(String id);
}
