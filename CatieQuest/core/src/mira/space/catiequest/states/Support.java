package mira.space.catiequest.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mira.space.catiequest.Button;
import mira.space.catiequest.CatieQuest;

/**
 * Экран, где можно купить поддержку или просмотреть рекламный ролик
 * */
public class Support extends State {

    private int width;
    private int height;

    private Texture backgroundSupport;
    private Button supButton;
    private Button supVideoButton;
    private Button backButton;

    public Support(GameStateManager gsm) {
        super(gsm);

        width = CatieQuest.getWidth();
        height = CatieQuest.getHeight();

        backgroundSupport = new Texture("sup_background.png");

        supButton = new Button(
                "sup_button.png",
                (40 * width) / 100,
                (40 * width) / 100 / 6,
                height / 2 - ((50 * width) / 100) / 2 - ((50 * width) / 100) / 10
        );

        supVideoButton = new Button(
                "video_button.png",
                (40 * width) / 100,
                width - (40 * width) / 100 - (40 * width) / 100 / 6,
                height / 2 - ((50 * width) / 100) / 2 - ((50 * width) / 100) / 10
        );

        backButton = new Button(
                "back_button.png",
                (20 * width) / 100,
                0,
                0
        );
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = height - Gdx.input.getY();
            if (backButton.isTouched(x, y)) {
                gsm.pop();
                return;
            }

            if (supButton.isTouched(x, y)) {
                CatieQuest.startSup();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(backgroundSupport, 0, 0, width, height);
        sb.draw(supButton.getButton(), supButton.getX(), supButton.getY(),
                supButton.getSize(), supButton.getSize());
        sb.draw(supVideoButton.getButton(), supVideoButton.getX(), supVideoButton.getY(),
                supVideoButton.getSize(), supVideoButton.getSize());
        sb.draw(backButton.getButton(), backButton.getX(), backButton.getY(),
                backButton.getSize(), backButton.getSize());
        sb.end();
    }

    @Override
    public void dispose() {
        backgroundSupport.dispose();
        supVideoButton.dispose();
        supButton.dispose();
        backButton.dispose();
    }
}
