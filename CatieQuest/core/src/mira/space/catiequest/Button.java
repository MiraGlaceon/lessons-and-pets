package mira.space.catiequest;

import com.badlogic.gdx.graphics.Texture;

/**
 * Кнопка. Здесь вычисляются ее координаты и размер, а также задается ее вид
 * */
public class Button implements Touchable{
    private Texture button;
    private int size;
    private int x;
    private int y;

    public Button(String path, int size, int x, int y) {
        button = new Texture(path);
        this.size = size;
        this.x = x;
        this.y = y;
    }

    public Texture getButton() {
        return button;
    }

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setButton(String path) {
        button.dispose();
        button = new Texture(path);
    }

    public boolean isTouched(int userX, int userY) {
        return (userX >= x && userX <= (x + size)) && userY >= y && userY <= (y + size);
    }

    public void dispose() {
        button.dispose();
    }
}
