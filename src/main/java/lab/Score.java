package lab;

import javafx.beans.property.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "score")
public class Score implements Serializable {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //private int id;
    @Id
    @Column (name = "Name", unique = true)
    String Name;
    @Column (name = "score")
    int  score;
    @Column (name = "shoots")
    int  shoots;
    @Column (name = "win")
    int  win;

    private static final long serialVersionUID = 1L;

    public Score(String N, int sc, int sh) {
        Name = N;
        score = sc;
        shoots = sh;
        win = 0;
    }
    public Score() { this("****", 0, 0); }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Score tmp = (Score) obj;

        if (tmp.Name.equals(this.Name)) { return true; }
        return false;
    }

    public String getName() {
        return Name;
    }

    public int getScore() {
        return score;
    }

    public int getShoots() {
        return shoots;
    }

    public int getWin() {
        return win;
    }

    @Override
    public String toString() {
        return "Score{" +
                "Name='" + Name + '\'' +
                ", score=" + score +
                ", shoots=" + shoots +
                ", win=" + win +
                '}';
    }
}
