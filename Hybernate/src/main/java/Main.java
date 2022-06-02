
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


/**
 * Использование Hibernate, чтобы заполнить новую таблицу, композитными ключами
 * */
public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "From " + PurchaseList.class.getSimpleName();
        List<PurchaseList> purchaseList = session.createQuery(hql).getResultList();

        int studentId;
        int courseId;
        Student student;
        Course course;
        Query query;
        Transaction transaction = session.beginTransaction();
        for (PurchaseList purchase : purchaseList) {
            hql = "From " + Student.class.getSimpleName() + " Where `name` = '" + purchase.getStudentName() + "'";
            student = (Student) session.createQuery(hql).getSingleResult();
            hql = "From " + Course.class.getSimpleName() + " Where `name` = '" + purchase.getCourseName() + "'";
            course = (Course) session.createQuery(hql).getSingleResult();
            studentId = student.getId();
            courseId = course.getId();
            hql = "insert LinkedPurchaseList (studentId, courseId) " +
                    "values (" + studentId + ", " + courseId + ")";
            query = session.createQuery(hql);
            query.executeUpdate();
        }
        transaction.commit();
        session.close();
    }
}
