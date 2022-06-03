package mira.space.catiequest.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

// Это менеджер экранов. Нужен для перемещения между экранами, запуска новых экранов
public class GameStateManager {

    private Stack<State> states;

    public GameStateManager() {
        this.states = new Stack<>();
    }

    public void push(State state) {
        states.push(state);
    }

    public void pop() {
        states.pop().dispose();
    }

    public void update(float dt) {
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }
}
