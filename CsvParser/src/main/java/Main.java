public class Main {

    public static Movements movements;

    //Код выводит Расходы по организациям.
    public static void main(String[] args) {
        movements = new Movements("C:\\Users\\1\\Desktop\\CsvParser\\src\\main\\resources\\movementList.csv");
        movements.printExpenseSumByOrganizations();
    }
}
