import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/* Класс читает файл csv банковской выписки movementsList.csv и парсит полученные строки.
Получение суммы расхода и дохода по всем операциями реализованы в классе
в методах getExpenseSum() и getIncomeSum() соответственно*/
public class Movements {

    public String PATH;
    public final int MAX_COLUMNS = 7; //количество столбцов в таблице -1
    public static String regex = "\"\\d+,\\d+\"";
    public static String dateRegex = "\\d{2}\\.";

    public Movements(String pathMovementsCsv) {
        PATH = pathMovementsCsv;
    }

    public void printCsv() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH));
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printExpenseSumByOrganizations() {
        HashMap<String, Double> organizations = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH));
            lines.remove(0);
            for (String line : lines) {
                line = refactorCsv(line);
                String[] columns = line.split(",");
                double value = Double.parseDouble(columns[MAX_COLUMNS].trim()); // последний столбец, это "расходы"
                String key = (columns[MAX_COLUMNS - 2]).substring(20);
                key = key.trim().split(dateRegex,3)[0].trim();
                if (organizations.containsKey(key)) {
                    organizations.put(key, organizations.get(key) + value);
                } else {
                    organizations.put(key, value);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("\nРасходы по организациям:");
        for (Map.Entry<String, Double> organization : organizations.entrySet()) {
            System.out.println(organization.getKey() + " : " + organization.getValue());
        }
    }

    //заменяет суммы через запятую на сумму через точку, убирает кавычки
    private String refactorCsv(String line) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return line.replaceAll(regex, matcher.group().replaceAll(",", "."))
                        .replaceAll("\"","");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return line;
    }

    public double getExpenseSum() {
        double sum = 0.0;
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH));
            lines.remove(0);
            for (String line : lines) {
                line = refactorCsv(line);
                String[] columns = line.split(",");
                sum += Double.parseDouble(columns[MAX_COLUMNS].trim()); // последний столбец, это "расходы"
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sum;
    }

    public double getIncomeSum() {
        double sum = 0.0;
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH));
            lines.remove(0);
            for (String line : lines) {
                line = refactorCsv(line);
                String[] columns = line.split(",");
                sum += Double.parseDouble(columns[MAX_COLUMNS - 1].trim()); // предпоследний столбец, это "приход"
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sum;
    }
}
