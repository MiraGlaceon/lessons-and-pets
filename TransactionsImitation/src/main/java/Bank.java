
import java.util.Map;
import java.util.Random;


/**
 * Предполагается, что класс будет работать в многопоточной среде. Во избежание deadlock`а
 * выстраивается приоритет захвата монитора.
 * Программа проверяется с помощью тестов
 * */
public class Bank{
    public final static long SUSPECT_SUM = 50_000L;

    private Map<String, Account> accounts;
    private final Random random = new Random();

    public void transaction(Account accountFrom, Account accountTo, long amount) {
        long fromMoney = accountFrom.getMoney();
        long toMoney = accountTo.getMoney();
        if (fromMoney >= amount) {
            accountFrom.setMoney(fromMoney - amount);
            accountTo.setMoney(toMoney + amount);
        } else {
            //оповещение клиента о недостатке денег
        }
    }

    //Имитация проверки службы безопасности
    private synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return random.nextBoolean();
    }

    /**
     * Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов
     * */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) {
        Account accountFrom = accounts.get(fromAccountNum);
        Account accountTo = accounts.get(toAccountNum);
        if (fromAccountNum.compareTo(toAccountNum) > 0) {
            synchronized (accountFrom) {
                synchronized (accountTo) {
                    transaction(accounts.get(fromAccountNum), accounts.get(toAccountNum), amount);
                }
            }
        } else {
            synchronized (accountTo) {
                synchronized (accountFrom) {
                    transaction(accounts.get(fromAccountNum), accounts.get(toAccountNum), amount);
                }
            }
        }

        if (amount >= SUSPECT_SUM && isFraud(fromAccountNum, toAccountNum, amount)) {
            //логика блокировки
        }
    }


    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getSumAllAccounts() {
        long sum = 0;
        for (Map.Entry<String, Account> account : accounts.entrySet()) {
            sum += account.getValue().getMoney();
        }
        return sum;
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }
}
