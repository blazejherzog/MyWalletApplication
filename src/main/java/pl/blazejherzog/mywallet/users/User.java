package pl.blazejherzog.mywallet.users;

import lombok.*;
import pl.blazejherzog.mywallet.Budget;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @NonNull
    @Column(name = "first_name")
    private String firstName;

    @NonNull
    @Column(name = "last_name")
    private String lastName;

    @NonNull
    @Column(name = "email")
    private String userEmail;

    @NonNull
    @Column(name = "password")
    private String password;

    @NonNull
    @OneToOne
    private Budget budget;
}
