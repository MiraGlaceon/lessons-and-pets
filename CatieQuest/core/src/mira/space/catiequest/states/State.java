package mira.space.catiequest.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State {

    protected OrthographicCamera camera;
    protected Vector3 vector;
    protected GameStateManager gsm;

    public State(GameStateManager gsm) {
        this.camera = new OrthographicCamera();
        this.vector = new Vector3();
        this.gsm = gsm;
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
