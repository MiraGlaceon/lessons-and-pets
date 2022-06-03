package mira.space.catiequest;

// интерфейс для реализации слушателя, если объект нажат
public interface Touchable {
    boolean isTouched(int userX, int userY);
}
