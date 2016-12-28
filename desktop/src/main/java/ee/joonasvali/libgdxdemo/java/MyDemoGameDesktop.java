package ee.joonasvali.libgdxdemo.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ee.joonasvali.libgdxdemo.core.SpaceShooterGame;

import java.awt.Toolkit;

public class MyDemoGameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Toolkit.getDefaultToolkit().getScreenSize().width;
		config.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		config.fullscreen = true;
		new LwjglApplication(new SpaceShooterGame(), config);
	}
}
