package ee.joonasvali.libgdxdemo.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ee.joonasvali.libgdxdemo.core.MyDemoGame;

public class MyDemoGameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MyDemoGame(), config);
	}
}
