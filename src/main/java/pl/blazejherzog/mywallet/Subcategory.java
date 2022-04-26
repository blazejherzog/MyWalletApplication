package pl.blazejherzog.mywallet;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "subcategories")
@NoArgsConstructor
@AllArgsConstructor
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