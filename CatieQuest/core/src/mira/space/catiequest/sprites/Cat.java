package mira.space.catiequest.sprites;

import com.badlogic.gdx.graphics.Texture;

import mira.space.catiequest.CatieQuest;
import mira.space.catiequest.Touchable;


/**
 * Класс Кота. Здесь подгружаеются его разные формы в определенные моменты или этапы в игре
 * */
public class Cat implements Touchable {
    private Texture cat;
    private int catSize;
    private int catX;
    private int catY;
    private String poseNoSpeak = "catPoseLeft1.png";
    private String poseSpeak = "catPoseLeft2.png";

    private boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private void setPose(){
        int currentDay = CatieQuest.getCurrentDay();
        if (isBetween(currentDay, 3, 4)) {
            poseNoSpeak = "catPoseRight1.png";
            poseSpeak = "catPoseRight2.png";
        } else if (currentDay == 5) {
            poseNoSpeak = "catPoseLeft1.png";
            poseSpeak = "catPoseLeft2.png";
        } else if (isBetween(currentDay, 6, 8)) {
            poseNoSpeak = "catPoseStraight1.png";
            poseSpeak = "catPoseStraight2.png";
        } else if (isBetween(currentDay, 9, 11)) {
            poseNoSpeak = "catPoseRight1.png";
            poseSpeak = "catPoseRight2.png";
        } else if (isBetween(currentDay, 12, 13)) {
            poseNoSpeak = "catPoseStraight1.png";
            poseSpeak = "catPoseStraight2.png";
        } else if (isBetween(currentDay, 14, 15)) {
            poseNoSpeak = "catPoseLeft1.png";
            poseSpeak = "catPoseLeft2.png";
        } else if (currentDay == 16) {
            poseNoSpeak = "catPoseRight1.png";
            poseSpeak = "catPoseRight2.png";
        }
    }

    public Cat() {
        setPose();
        cat = new Texture(poseNoSpeak);

        catSize = (40 * CatieQuest.getHeight()) / 100;
        catX = (CatieQuest.getWidth() - catSize) - (CatieQuest.getWidth() - catSize) / 2;
        catY = catSize / 5;
    }

    public int getCatSize() {
        return catSize;
    }

    public int getCatX() {
        return catX;
    }

    public int getCatY() {
        return catY;
    }

    public Texture getCat() {
        return cat;
    }

    public void speak() {
        cat.dispose();
        cat = new Texture(poseSpeak);
    }

    public void noSpeak() {
        cat.dispose();
        cat = new Texture(poseNoSpeak);
    }

    public void sleep() {
        cat.dispose();
        cat = new Texture("catPoseSleep.png");
    }

    public void dispose() {
        cat.dispose();
    }

    @Override
    public boolean isTouched(int userX, int userY) {
        return (userX >= catX && userX <= (catX + catSize)) && userY >= catY && userY <= (catY + catSize);
    }
}
