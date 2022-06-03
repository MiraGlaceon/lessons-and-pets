package mira.space.catiequest.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mira.space.catiequest.Button;
import mira.space.catiequest.CatieQuest;
import mira.space.catiequest.TapButton;
import mira.space.catiequest.sprites.Cat;
import mira.space.catiequest.sprites.Dialog;


/**
 * Экран с основным игровым процессом
 * */
public class PlayState extends State {
    public static final String DATE_FORMAT = "dd.MM.yyyy H";

    // в массиве содержится лента диалога на каждый день и ее размер
    private ArrayList<String> days = new ArrayList<String>()
    {{
        add("dialogsDay1.png 3 7");
        add("dialogsDay2.png 1 3");
        add("dialogsDay3.png 1 5");
        add("dialogsDay4.png 1 5");
        add("dialogsDay5.png 1 6");
        add("dialogsDay6.png 2 11");
        add("dialogsDay7.png 1 3");
        add("dialogsDay8.png 1 9");
        add("dialogsDay9.png 1 8");
        add("dialogsDay10.png 1 9");
        add("dialogsDay11.png 1 6");
        add("dialogsDay12.png 6 9");
        add("dialogsDay13.png 1 5");
        add("dialogsDay14.png 1 8");
        add("dialogsDay15.png 1 8");
        add("dialogsDay16.png 99 9");
        add("dialogsDay17.png 99 9");
    }};

    private Cat cat;
    private TapButton tapButton;
    private Button bowl;
    private Dialog dialog;

    private int width;
    private int height;

    private Texture background;
    private Texture dialogWhenCatSleep;
    private Texture currentDialog;

    private boolean isDialogStarted;
    private boolean needTapButton;
    private boolean isCatSleep;
    private boolean isCatTouchedWhenSleep;
    private boolean gameOver;
    private boolean isButtonDisappeared;

    public static boolean isSupped;
    public static boolean isButtonPressed;
    public static boolean isGameRunning;
    public static boolean isActFinished;

    private String lastDate;
    private int currentDay;

    private int dialogsCount;
    private int numberWhenDialogDisappears;

    public PlayState(GameStateManager gsm, int currentDay, String lastDate, boolean hasSupped) throws ParseException {
        super(gsm);

        width = CatieQuest.getWidth();
        height = CatieQuest.getHeight();

        tapButton = new TapButton(
                "nonpressed.png",
                (20 * width) / 100,
                width - ((20 * width) / 100) * 2,
                0);

        bowl = new Button(
                "bowl_empty.png",
                (20 * width) / 100,
                0,
                (20 * width) / 100 / 2
        );

        cat = new Cat();
        this.currentDay = currentDay;
        this.lastDate = lastDate;

        background = new Texture("background.png");
        dialogWhenCatSleep = new Texture("dialogCatSleep.png");

        needTapButton = false;
        isCatSleep = false;
        isCatTouchedWhenSleep = false;
        isDialogStarted = false;
        gameOver = false;
        isButtonDisappeared = currentDay == 15;

        isButtonPressed = false;
        isGameRunning = true;
        isSupped = hasSupped;
        isActFinished = false;

        initializeDay();
    }

    // здесь обновляем данные, если неаступил новый день
    public void initializeDay() throws ParseException {
        if (currentDay >= 17) {
            gameOver = true;
            isGameRunning = false;
            return;
        }

        if (currentDay != 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(lastDate));
            cal.add(Calendar.HOUR_OF_DAY, 12);

            boolean isItTime = (new Date()).after(cal.getTime());

            if (!isItTime) {
                isCatSleep = true;
                isGameRunning = false;
                cat.sleep();
                dialog = new Dialog(new TextureRegion(dialogWhenCatSleep), 1);
                isActFinished = true;
                return;
            }
        }

        String[] dayInParts = days.get(currentDay).split(" ");
        // dayInParts[0] - дата последнего визита
        // dayInParts[1] - индекс, когда диалог должен пропасть
        // dayInParts[2] - общее количество диалогов в ленте

        dialogsCount = Integer.parseInt(dayInParts[2]);
        currentDialog = new Texture(dayInParts[0]);
        dialog = new Dialog(new TextureRegion(currentDialog), dialogsCount + 1);
        numberWhenDialogDisappears = Integer.parseInt(dayInParts[1]);

        currentDay++;

        // сохраняем процесс и запускаем новый день
        CatieQuest.save(currentDay);
        CatieQuest.setSup();
    }

    // метод реагирует на нажатие кнопки или миски
    public void checkBowlOrButtonTapped(int x, int y) {
        if (tapButton.isTouched(x, y)) {
            isButtonPressed = true;
            tapButton.setButton("pressed.png");
        }

        if (bowl.isTouched(x, y)) {
            gsm.push(new Support(gsm));
            if (isSupped) {
                bowl.setButton("bowl_fish.png");
            }
        }
    }

    @Override
    public void handleInput() {
        // если акт закончен
        if (Gdx.input.justTouched() && (!isGameRunning || gameOver)) {
            int x = Gdx.input.getX();
            int y = height - Gdx.input.getY();
            if (cat.isTouched(x, y) && isCatSleep) {
                isCatTouchedWhenSleep = true;
            } else {
                checkBowlOrButtonTapped(x, y);
            }
            return;
        }

        if (Gdx.input.justTouched() && !needTapButton) {
            // завершаем диалог
            if(dialog.getDialogsCount() >= dialogsCount) {
                isDialogStarted = false;
                isGameRunning = false;
                cat.noSpeak();
                isActFinished = true;

            // или просим нажать на кнопку
            } else if (dialog.getDialogsCount() == numberWhenDialogDisappears) {
                needTapButton = true;
                isDialogStarted = false;
                cat.noSpeak();

            // или просто пролистываем диалог дальше
            } else {
                dialog.update();
                isDialogStarted = true;
                cat.speak();
            }

            return;
        }

        // нужно нажать на кнопку
        if (needTapButton) {
            int x = Gdx.input.getX();
            int y = height - Gdx.input.getY();
            if (tapButton.isTouched(x, y)) {
                needTapButton = false;
                dialog.update();
                cat.speak();
                isDialogStarted = true;
                isButtonPressed = true;
                tapButton.setButton("pressed.png");
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if (isButtonPressed) {
            tapButton.update(dt);
        }
    }

    // метод рисует все объекты на экране и обновляет их
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, width, height);
        // показывать ли диалог
        if (isDialogStarted) {
            sb.draw(dialog.getDialog(), dialog.getDialogX(), dialog.getDialogY(),
                    dialog.getDialogSize(), dialog.getDialogSize());
        }

        // показывать ли диалог когда кот спит
        if (isCatTouchedWhenSleep) {
            sb.draw(dialogWhenCatSleep, dialog.getDialogX(), dialog.getDialogY(),
                    dialog.getDialogSize(), dialog.getDialogSize());
        }

        // отображать кота или нет
        if (!gameOver) {
            sb.draw(cat.getCat(), cat.getCatX(), cat.getCatY(),
                    cat.getCatSize(), cat.getCatSize());
        }

        // отображать кнопку или нет
        if (!isButtonDisappeared) {
            sb.draw(tapButton.getButton(), tapButton.getX(), tapButton.getY(),
                    tapButton.getSize(), tapButton.getSize());
        }

        // отображение миски
        sb.draw(bowl.getButton(), bowl.getX(), bowl.getY(),
                bowl.getSize(), bowl.getSize());
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        cat.dispose();
        tapButton.dispose();
        bowl.dispose();
        currentDialog.dispose();
        dialogWhenCatSleep.dispose();
    }
}
