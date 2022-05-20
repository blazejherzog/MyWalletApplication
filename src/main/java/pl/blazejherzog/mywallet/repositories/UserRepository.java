package pl.blazejherzog.mywallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.blazejherzog.mywallet.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserEmail(String email);
    Optional<User> findByNickName (String nickname);
}
