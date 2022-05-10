package pl.blazejherzog.mywallet.months;

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
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private int year;
}
