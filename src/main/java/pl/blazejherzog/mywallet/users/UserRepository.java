package pl.blazejherzog.mywallet.users;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blazejherzog.mywallet.users.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String email);
}
