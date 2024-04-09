package lab;

import org.hibernate.*;

import java.util.List;

public abstract class DAO_HB<T> {
    protected MySessionFactory mySessionFactory;

    protected DAO_HB(Class<T> cl) {}

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

    public List<T> getAll() {
        return mySessionFactory.getSessionFactory().openSession().createQuery("From Score").list();
    }
}
