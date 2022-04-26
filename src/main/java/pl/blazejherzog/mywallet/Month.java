package pl.blazejherzog.mywallet;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "months")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Month {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private int year;
}
