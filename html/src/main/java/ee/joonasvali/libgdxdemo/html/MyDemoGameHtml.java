package ee.joonasvali.libgdxdemo.html;

import ee.joonasvali.libgdxdemo.core.MyDemoGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class MyDemoGameHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new MyDemoGame();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
