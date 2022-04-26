package pl.blazejherzog.mywallet;

import lombok.*;

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

}
