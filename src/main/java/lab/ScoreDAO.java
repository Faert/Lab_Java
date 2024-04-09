package lab;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ScoreDAO extends DAO_HB<Score> {
    public ScoreDAO (){
        super(Score.class);
    }

    public void Sc_addOrUpdate(Score s) {
        Session session = mySessionFactory.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        Score tmp = session.get(Score.class, s.Name);
        if(tmp == null) {
            session.save(s);
        } else {
            tmp.score += s.score;
            tmp.shoots += s.shoots;
            session.merge(tmp);
        }

        tr.commit();
        session.close();
    }

    public void printAll() {
        List<Score> scoreList = getAll();
        System.out.println("\nDataBase list:");
        for (int i = 0; i < scoreList.size(); i++) {
            System.out.println(scoreList.get(i).Name + " Sc: " + scoreList.get(i).score + " Sh: " + scoreList.get(i).shoots);
        }
    }
}
