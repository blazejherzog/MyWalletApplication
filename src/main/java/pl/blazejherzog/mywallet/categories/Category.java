package pl.blazejherzog.mywallet.categories;

import lombok.*;
import pl.blazejherzog.mywallet.budgets.Budget;
import pl.blazejherzog.mywallet.subcategories.Subcategory;
import pl.blazejherzog.mywallet.users.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

}
