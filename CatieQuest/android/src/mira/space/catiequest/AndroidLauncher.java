package mira.space.catiequest;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mira.space.catiequest.states.PlayState;

public class AndroidLauncher extends AndroidApplication implements AccessToActivity {

	private String lastDate;
	private int currentDay;
	private SharedPreferences pref;

	public static final String KEY_ACT = "act";
	public static final String KEY_CURRENT_DAY = "day";
	public static final String KEY_LAST_DATE = "date";
	public static final String KEY_SUPPORTED = "support";
	public static final String DATE_FORMAT = "dd.MM.yyyy H";

	//ниже переменные для покупок
	private static final String inAppItem1 = "sup1dol";
	private BillingClient billingClient;
	private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
		@Override
		public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {
			// сюда мы попадем когда будет осуществлена покупка
			// здесь кладем в миску рыбу
			if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
				for (Purchase purchase : list) {
					if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
						ConsumeParams consumeParams = ConsumeParams.newBuilder()
								.setPurchaseToken(purchase.getPurchaseToken())
								.build();

						billingClient.consumeAsync(
								consumeParams,
								(billingResult1, s) -> {
									PlayState.isSupped = true;
									pref = getPreferences(MODE_PRIVATE);
									SharedPreferences.Editor editor = pref.edit();
									editor.putBoolean(KEY_SUPPORTED, true);
									editor.commit();
								}
						);

					}
				}
			}
		}
	};
	private Map<String, SkuDetails> mSkuDetailsMap = new HashMap<>();

	private int width;
	private int height;

	AndroidApplicationConfiguration config;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		config = new AndroidApplicationConfiguration();

		// инициализация внутреигровых покупок
		billingClient = BillingClient.newBuilder(this)
				.setListener(purchasesUpdatedListener)
				.enablePendingPurchases()
				.build();

		connectToGooglePlayBilling();

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;

		config.useAccelerometer = false;
		config.useCompass = false;

		// восстанавливаем данные, если приложение запускается заново
		pref = getPreferences(MODE_PRIVATE);
		currentDay = pref.getInt(KEY_CURRENT_DAY, 0);
		lastDate = pref.getString(KEY_LAST_DATE,
				new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(new Date()));
		boolean hasSupped = pref.getBoolean(KEY_SUPPORTED, false);

		// здесь передаем необходимые параметры и запускаем работу с libGDX
		initialize(new CatieQuest(this, width, height, currentDay, lastDate, hasSupped), config);
	}

	// устанавливает метку, что бонус при поддержке приложения закончился
	public void setSup() {
		pref = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_SUPPORTED, false);
		editor.commit();
	}

	// если пользователь свернул приложение или отвлекся, то приложение возвражает на начало этапа
	// это нужно, чтобы пользователь не пропустил историю, если в следующий раз вернется через сутки
	@Override
	protected void onPause() {
		if (PlayState.isActFinished) {
			pref = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(KEY_ACT, "reset");
			editor.commit();
		}
		super.onPause();
	}

	// возвращает данные на место, если пользователь возвращается в игру
	@Override
	protected void onResume() {
		pref = getPreferences(MODE_PRIVATE);
		String result = pref.getString(KEY_ACT, "itsOk");
		if (result.equals("reset")){
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(KEY_ACT, "itsOk");
			editor.commit();
			currentDay = pref.getInt(KEY_CURRENT_DAY, 0);
			lastDate = pref.getString(KEY_LAST_DATE,
					new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(new Date()));
			boolean hasSupped = pref.getBoolean(KEY_SUPPORTED, false);
			initialize(new CatieQuest(this, width, height, currentDay, lastDate, hasSupped), config);
		}
		super.onResume();
	}

	// в методе сохраняется игровой процесс при обстоятельствах, когда главная активность уничтожается
	@Override
	protected void onDestroy() {
		CatieQuest.randomlyDestroyed();
		super.onDestroy();
	}

	// сохранение данных, когда пользователь прошел этам
	public void saveData(int day, String date) {
		pref = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(KEY_CURRENT_DAY, day);
		if (date.equals("nextDate")) {
			editor.putString(KEY_LAST_DATE, new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(new Date()));
		} else {
			editor.putString(KEY_LAST_DATE, lastDate);
		}
		editor.commit();
	}

	// соединение с гуглом, чтобы можно было совершать покупки
	private void connectToGooglePlayBilling() {
		billingClient.startConnection(new BillingClientStateListener() {
			@Override
			public void onBillingServiceDisconnected() {
				connectToGooglePlayBilling();
			}

			@Override
			public void onBillingSetupFinished(BillingResult billingResult) {
				if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
					querySkuDetails();
				}
			}
		});
	}

	// добавляем в детали покупки доступные предметы для покупки
	private void querySkuDetails(){
		List<String> skuList  = new ArrayList<>();
		skuList.add(inAppItem1);
		SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
		params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
		billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
			@Override
			public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
				if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK &&
						 list != null){
					for (SkuDetails skuDetails : list) {
						mSkuDetailsMap.put(skuDetails.getSku(), skuDetails);
					}
				}
			}
		});
	}

	// запускает окно, где можно посмотреть предмет, его описание и цену, также купить
	public void startPurchase() {
		if (mSkuDetailsMap.size() > 0) {
			BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
					.setSkuDetails(mSkuDetailsMap.get(inAppItem1))
					.build();

			billingClient.launchBillingFlow(this, billingFlowParams);
		}
	}

}
