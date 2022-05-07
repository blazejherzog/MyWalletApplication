package pl.blazejherzog.mywallet;

import lombok.*;
import pl.blazejherzog.mywallet.users.User;

import javax.persistence.*;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int budgetId;

    @Column(name = "amount")
    private int amount;

    @OneToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
