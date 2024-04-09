package lab;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestMain {
    public static void main(String[] args) {
        MyPair sc = new MyPair(1, 1);
        sc.setSc(1);
        sc.setSh(1);
        Session session = MySessionFactory.getSessionFactory().openSession();
        Transaction tr =  session.beginTransaction();
        System.out.println("!!!!!!!!!!!!!");
        System.out.println(tr.getStatus());
        System.out.println(session.isOpen());
        session.saveOrUpdate(sc);
        System.out.println("AAAAAAAAAAAA");
        tr.commit();
        session.close();
    }
}
