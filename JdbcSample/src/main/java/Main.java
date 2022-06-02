import java.sql.*;

public class Main {

    public static String URL = "jdbc:mysql://localhost:3306/skillbox";
    public static String USER = "root";
    public static String PASSWORD = "root";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            String query = "SELECT `course_name`, COUNT(*) `count`, " +
                    "COUNT(*) / (MONTH(MAX(`subscription_date`)) - MONTH(MIN(`subscription_date`)) + 1) " +
                    "AS `avr_per_month` " +
                    "FROM `purchaselist`" +
                    "GROUP BY `course_name`";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                String count = resultSet.getString("count");
                String averagePerMonth = resultSet.getString("avr_per_month");
                System.out.println(courseName + " " + count + " " + averagePerMonth);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
