package lab;

import javax.persistence.*;
@Entity
@Table (name = "score")
public class Score {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //private int id;
    @Id
    @Column (name = "Name", unique = true)
    String Name;
    @Column (name = "score")
    int score;
    @Column (name = "shoots")
    int shoots;

    public Score(String N, int sc, int sh) {
        Name = N;
        score = sc;
        shoots = sh;
    }
    public Score() { this("****", 0, 0); }

}
