import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class BankTest extends TestCase {

    Bank bank;
    Map<String, Account> accounts;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bank = new Bank();
        accounts = new HashMap<>();
        accounts.put("one", new Account(50000L, "one"));
        accounts.put("two", new Account(25000L, "two"));
        accounts.put("three", new Account(75000L, "three"));
        accounts.put("four", new Account(0, "four"));
        accounts.put("five", new Account(30000L, "five"));
        accounts.put("six", new Account(60000L, "six"));
        bank.setAccounts(accounts);
    }

    private class BankWorkSimulator extends Thread {

        private Bank testBank;
        private String fromName;
        private String toName;
        private long amount;
        CountDownLatch countDownLatch;

        public BankWorkSimulator(Bank testBank, String fromName, String toName, long amount, CountDownLatch countDownLatch) {
            this.testBank = testBank;
            this.fromName = fromName;
            this.toName = toName;
            this.amount = amount;
            this.countDownLatch = countDownLatch;
            start();
        }

        @Override
        public void run() {
            testBank.transfer(fromName, toName, amount);
            countDownLatch.countDown();
        }
    }

    public void testTransfer() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        BankWorkSimulator testBank;
        long actual = bank.getSumAllAccounts();
        long expected = 240000L;
        assertEquals(expected, actual);
        // В цикле проверяется 2 аккаунта, чтобы не было deadlock`a
        // при проверке закомментировать секунду ожидания в Bank.class метод isFraud
        for (int i = 0; i < 100000; i++) {
            testBank = new BankWorkSimulator(bank, "six", "two", 50000L, countDownLatch);
            testBank = new BankWorkSimulator(bank, "two", "six", 50000L, countDownLatch);
//            testBank = new BankWorkSimulator(bank, "four", "six", 50000L, countDownLatch);
//            testBank = new BankWorkSimulator(bank, "five", "four", 50000L, countDownLatch);
//            testBank = new BankWorkSimulator(bank, "three", "four", 50000L, countDownLatch);
//            testBank = new BankWorkSimulator(bank, "three", "four", 50000L, countDownLatch);
//            testBank = new BankWorkSimulator(bank, "six", "one", 50000L, countDownLatch);
            countDownLatch.await();
            actual = bank.getSumAllAccounts();
            assertEquals(expected, actual);
            countDownLatch = new CountDownLatch(2);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
