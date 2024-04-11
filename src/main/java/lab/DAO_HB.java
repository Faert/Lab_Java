package lab;

import org.hibernate.*;

import java.sql.Array;
import java.util.List;

public abstract class DAO_HB<T> {
    protected MySessionFactory mySessionFactory;

    List<T> data;

    protected DAO_HB(Class<T> cl) {};

    protected Session getCurrentSession() {
        return mySessionFactory.getSessionFactory().getCurrentSession();
    }
    public void addOrUpdate(T s, String key) {
        Session session = mySessionFactory.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        if(session.get(s.getClass(), key) == null) {
            session.save(s);
        } else {
            session.merge(s);
        }

        tr.commit();
        session.close();
    }

    public void getAll() {
        if(data == null){
            data = mySessionFactory.getSessionFactory().openSession().createQuery("From Score").list();
        }
    }
}
