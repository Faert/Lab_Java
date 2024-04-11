package lab;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;

public class ScoreDAO extends DAO_HB<Score> {
    public ScoreDAO (){
        super(Score.class);
    }
    //List<Integer> ind;

    public void Sc_addOrUpdate(Score s) {
        System.out.println(s.toString());
        Session session = mySessionFactory.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        Score tmp = session.get(Score.class, s.Name);
        if (data == null) {
            getAll();
        }
        if(tmp == null) {
            session.save(s);
            data.add(s);
        } else {
            int ind = data.indexOf(tmp);
            tmp.score += s.score;
            tmp.shoots += s.shoots;
            tmp.win += s.win;
            data.set(ind, tmp);
            session.merge(tmp);
        }

        tr.commit();
        session.close();
        System.out.println(data.getLast().toString());
    }

    public List<Score> printAll() {
        if(data == null) {
            getAll();
        }

        System.out.println("\nDataBase list:");
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i).Name + " Sc: " + data.get(i).score + " Sh: " + data.get(i).shoots
                    + " Win: " +data.get(i).win);
        }
        return data;
    }
}
