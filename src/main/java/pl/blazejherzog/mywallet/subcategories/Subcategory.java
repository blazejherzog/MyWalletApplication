package pl.blazejherzog.mywallet.subcategories;

import lombok.*;
import pl.blazejherzog.mywallet.categories.Category;

import javax.persistence.*;

@Entity
@Table(name = "subcategories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Category category;
}