package pl.blazejherzog.mywallet.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "budgeted_date")
    private LocalDate budgetedDate;

    @Column(name = "budgeted_amount")
    private int budgetedAmount;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id")
    private Category category;
}
