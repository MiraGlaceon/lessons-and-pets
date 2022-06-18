package space.mira;

import org.hibernate.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class Main {

    public static void main(String[] args) throws IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command;
        boolean flag = true;

        System.out.println("\n\nДоступные команды (дату рождения вводите в формате 2000-12-31):\n" +
                "insert ИМЯ ФАМИЛИЯ ОТЧЕСТВО ДАТА_РОЖДЕНИЯ ГРУППА\n" +
                "delete ID\n" +
                "list\n" +
                "exit\n");
        while (flag) {
            System.out.println("Введите команду:");
            command = reader.readLine().trim();
            if (command.equals("")) {
                System.out.println("Введите команду:");
                continue;
            }

            switch (command.split(" ")[0].toLowerCase(Locale.ROOT)) {
                case "insert":
                    CrudStudent.insert(session, command);
                    break;
                case "delete":
                    CrudStudent.delete(session, command);
                    break;
                case "list":
                    ArrayList<Student> students = (ArrayList<Student>) CrudStudent.list(session);
                    if (students.isEmpty()) {
                        System.out.println("Нет студентов в БД");
                    } else {
                        students.stream().forEach(System.out::println);
                    }
                    break;
                case "exit":
                    flag = false;
            }
        }

        session.close();
    }
}
