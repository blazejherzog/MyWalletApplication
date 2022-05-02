package pl.blazejherzog.mywallet;

import lombok.*;
import pl.blazejherzog.mywallet.subcategories.Subcategory;

import javax.persistence.*;

@Entity
@Table(name = "budgeted_amounts_per_subcategories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BudgetedAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Subcategory subcategory;

    @ManyToOne
    private Month month;

    @Column(name = "budgeted_amount")
    private int budgetedAmount;
}
