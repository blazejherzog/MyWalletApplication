package pl.blazejherzog.mywallet.categories;

import lombok.*;
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
    private User user;

    @OneToMany(mappedBy = "category")
    private List<Subcategory> subcategoryList;
}
