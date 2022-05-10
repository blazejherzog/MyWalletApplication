package pl.blazejherzog.mywallet.transactions;

import lombok.*;
import pl.blazejherzog.mywallet.budgets.Budget;
import pl.blazejherzog.mywallet.subcategories.Subcategory;
import pl.blazejherzog.mywallet.users.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount")
    private int amount;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

}
