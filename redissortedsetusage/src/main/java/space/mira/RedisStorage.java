package space.mira;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.Collection;

public class RedisStorage {

    public static final String KEY = "users_on_site";
    private RScoredSortedSet<String> users;

    public RedisStorage() {
        init();
    }

    private void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        Redisson redisson = (Redisson) Redisson.create(config);
        RKeys rKeys = redisson.getKeys();
        users = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    public void removeUser(String user) {
        users.remove(user);
    }

    public void addUser(int priority, String user) {
        users.add(priority, user);
    }

    public String getNextUser() {
        String user = users.first();
        removeUser(user);
        return user;
    }

    public String getRandomUser() {
        int score = (int) (Math.random() * users.size());
        Collection<String> values = users.valueRange(score, score);
        if (values.isEmpty()) {
            return getNextUser();
        }
        String user = ((ArrayList<String>) values).get(0);
        users.addScore(user, Integer.MIN_VALUE);
        return getNextUser();
    }

}
