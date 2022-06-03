package mira.space.catiequest;

import mira.space.catiequest.states.PlayState;

/**
 * Здесь обрабатывается нажатие на кнопку. Она нажимается и возврращается в прежнее положение
 * */
public class TapButton extends Button {
    private float periodWhenButtonIsPressed;

    public TapButton(String path, int size, int x, int y) {
        super(path, size, x, y);
        periodWhenButtonIsPressed = 0;
    }

    public void update(float dt) {
        periodWhenButtonIsPressed += dt;
        if (periodWhenButtonIsPressed >= 0.05f) {
            setButton("nonpressed.png");
            periodWhenButtonIsPressed = 0;
            PlayState.isButtonPressed = false;
        }
    }
}
