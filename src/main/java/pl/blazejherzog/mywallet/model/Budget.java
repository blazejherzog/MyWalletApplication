package pl.blazejherzog.mywallet.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
