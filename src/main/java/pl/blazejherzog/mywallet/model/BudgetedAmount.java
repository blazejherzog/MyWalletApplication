package pl.blazejherzog.mywallet.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "budgeted_amounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BudgetedAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "month_id", nullable = false)
    private Month month;

    @Column(name = "budgeted_amount")
    private int budgetedAmount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
