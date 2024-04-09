package lab;

import javax.persistence.*;
@Entity
@Table (name = "mypair")
public class MyPair {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    //@Column(name="id", unique = true, nullable = false)
    private int id;
    @Column(name = "sc")
    private int sc;
    @Column(name = "sh")
    private int sh;

    public MyPair() {}

    public MyPair(int a, int b)
    {
        sc = a;
        sh = b;
    }

    public int getId() {
        return id;
    }

    public int getSc() {
        return sc;
    }

    public int getSh() {
        return sh;
    }

    public void setSc(int sc) {
        this.sc = sc;
    }

    public void setSh(int sh) {
        this.sh = sh;
    }
}
