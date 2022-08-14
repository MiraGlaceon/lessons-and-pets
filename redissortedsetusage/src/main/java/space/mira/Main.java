package space.mira;

public class Main {

    public static final int USERS_COUNT = 20;
    private static RedisStorage redisStorage;

    public static void main(String[] args) {
        redisStorage = new RedisStorage();

        // Есть набор из 20ти пользователей
        setUsersList();

        String user;
        int step = 0;

        // Выводятся пользователи 100 раз
        while (step <= 100) {
            if (isUserBoughtPass()) {
                user = redisStorage.getRandomUser();
                System.out.println("> Пользователь " + user + " оплатил платную услугу\n" +
                        "— На главной странице показываем " + user);
            } else {
                user = redisStorage.getNextUser();
                System.out.println("— На главной странице показываем " + user);
            }

            step++;
            if (step % USERS_COUNT == 0) {
                setUsersList();
            }
        }
    }

    // создает список пользователей
    private static void setUsersList() {
        for (int i = 1; i <= USERS_COUNT; i++) {
            redisStorage.addUser(i, "Пользователь " + i);
        }
    }

    // В одном из 10 случаев случайный пользователь оплачивает услугу
    public static boolean isUserBoughtPass() {
        return (int) (Math.random() * 10) == 9;
    }
}
