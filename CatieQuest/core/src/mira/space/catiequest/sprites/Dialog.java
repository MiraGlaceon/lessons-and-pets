package mira.space.catiequest.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import mira.space.catiequest.CatieQuest;

/**
 * Здесь обрабатывается лента диалогов и можно получать диалоги по отдельности
 * */
public class Dialog {
    private Array<TextureRegion> dialogs;
    private int dialogsCount;
    private int dialog;

    private int dialogSize;
    private int dialogX;
    private int dialogY;

    public Dialog(TextureRegion region, int dialogsCount) {
        dialogs = new Array<>();
        int frameWidth = region.getRegionWidth() / dialogsCount;
        for (int i = 0; i < dialogsCount; i++) {
            dialogs.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }

        this.dialogsCount = dialogsCount;
        dialog = -1;

        int width = CatieQuest.getWidth();
        dialogSize = (90 * width) / 100;
        dialogX = (width - dialogSize) - (width - dialogSize) / 2;
        dialogY = CatieQuest.getHeight() - dialogSize;
    }

    public void update() {
        dialog++;
    }

    public TextureRegion getDialog() {
        return dialogs.get(dialog);
    }

    public int getDialogsCount() {
        return dialog;
    }

    public int getDialogSize() {
        return dialogSize;
    }

    public int getDialogX() {
        return dialogX;
    }

    public int getDialogY() {
        return dialogY;
    }
}
