package ee.joonasvali.libgdxdemo.android;

import ee.joonasvali.libgdxdemo.core.MyDemoGame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MyDemoGameActivity extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			initialize(new MyDemoGame(), config);
	}
}
