import java.util.Collections;
import java.util.List;

public class Main {

    public static final String STAFF_TXT = "C:\\collection\\projects\\LessonsAndPets\\LambdaAndFiles\\src\\main\\resources\\staff.txt";

    public static void main(String[] args) {
        List<Employee> staff = Employee.loadStaffFromFile(STAFF_TXT);
        sortBySalaryAndAlphabet(staff);
        print(staff);
    }

    public static void print(List<Employee> list) {
        for (Employee e : list) {
            System.out.println(e.toString());
        }
    }

    public static void sortBySalaryAndAlphabet(List<Employee> staff) {
        Collections.sort(staff, ((o1, o2) -> {
            if (o1.getSalary().equals(o2.getSalary())) {
                return o1.getName().compareTo(o2.getName());
            } else {
                return o1.getSalary().compareTo(o2.getSalary());
            }
        }));
    }
}