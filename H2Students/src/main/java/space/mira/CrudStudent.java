package space.mira;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CrudStudent {

    public static final int INSERT_ARGS_COUNT = 6;
    public static final int DELETE_ARGS_COUNT = 2;
    public static final int NAME_ARG = 1;
    public static final int LAST_NAME_ARG = 2;
    public static final int PATRONYMIC_ARG = 3;
    public static final int DATE_ARG = 4;
    public static final int GROUP_ARG = 5;
    public static final int ID_ARG = 1;
    public static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final String DIGITAL_REGEX = "\\d+";

    public static void insert(Session session, String commandLine) {
        commandLine = commandLine.replaceAll(" +", " ");
        String[] fields = commandLine.split(" ");
        if (fields.length != INSERT_ARGS_COUNT) {
            System.out.println("Кол-во вводимых слов должно быть " + INSERT_ARGS_COUNT);
            return;
        }

        if (!fields[DATE_ARG].matches(DATE_REGEX)) {
            System.out.println("Некорректный формат даты. Нужный формат: 2000-12-31");
            return;
        }

        Transaction transaction = session.beginTransaction();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            session.save(new Student(
                    fields[NAME_ARG],
                    fields[LAST_NAME_ARG],
                    fields[PATRONYMIC_ARG],
                    format.parse(fields[DATE_ARG]),
                    fields[GROUP_ARG]
            ));
            transaction.commit();
        } catch (ParseException e) {
            System.out.println("Ошибка при обработке даты");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void delete(Session session, String commandLine) {
        commandLine = commandLine.replaceAll(" +", " ");
        String[] fields = commandLine.split(" ");
        if (fields.length != DELETE_ARGS_COUNT) {
            System.out.println("Кол-во вводимых слов должно быть " + DELETE_ARGS_COUNT);
            return;
        }

        if (!fields[ID_ARG].matches(DIGITAL_REGEX)) {
            System.out.println("ID должен быть положительным числом");
            return;
        }

        Transaction transaction = session.beginTransaction();
        try {
            session.remove(session.find(Student.class, fields[ID_ARG]));
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Нет студента с ID = " + fields[ID_ARG]);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static List<Student> list(Session session) {
        String hql = "From " + Student.class.getSimpleName();
        return session.createQuery(hql).getResultList();
    }
}
