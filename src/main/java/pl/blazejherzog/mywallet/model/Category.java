package pl.blazejherzog.mywallet.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "budget_id")
    private Budget budget;

}
