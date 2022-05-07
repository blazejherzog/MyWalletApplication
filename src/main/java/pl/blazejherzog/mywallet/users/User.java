package pl.blazejherzog.mywallet.users;

import lombok.*;
import pl.blazejherzog.mywallet.Budget;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(name = "nickname")
    private String nickName;


    @Column(name = "first_name")
    private String firstName;


    @Column(name = "last_name")
    private String lastName;


    @Column(name = "email")
    private String userEmail;


    @Column(name = "password")
    private String password;


//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
//    private Budget budget;
}
