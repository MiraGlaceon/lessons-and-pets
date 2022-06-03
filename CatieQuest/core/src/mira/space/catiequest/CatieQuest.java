package mira.space.catiequest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.text.ParseException;

import mira.space.catiequest.states.GameStateManager;
import mira.space.catiequest.states.PlayState;

public class CatieQuest extends ApplicationAdapter {

	private static AccessToActivity context;

	private static int width;
	private static int height;
	private static int currentDay;
	private static String lastDate;
	private static GameStateManager gsm;
	private SpriteBatch batch;
	private static boolean hasSupped;

	public CatieQuest(AccessToActivity context, int width, int height, int currentDay, String lastDate, boolean hasSupped) {
		CatieQuest.context = context;
		CatieQuest.width = width;
		CatieQuest.height = height;
		CatieQuest.currentDay = currentDay;
		CatieQuest.lastDate = lastDate;
		CatieQuest.hasSupped = hasSupped;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static int getCurrentDay() {
		return currentDay;
	}

	public static void save(int numberOfTheDayToSave) {
		context.saveData(numberOfTheDayToSave, "nextDate");
	}
	public static void startSup() {
		context.startPurchase();
	}
	public static void setSup() { context.setSup(); }

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		ScreenUtils.clear(1, 0, 0, 1);
		try {
			gsm.push(new PlayState(gsm, currentDay, lastDate, hasSupped));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	// если приложение случайно обрушилось, то сохнаняем процесс
	public static void randomlyDestroyed() {
		if (PlayState.isGameRunning) {
			context.saveData(currentDay, lastDate);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
