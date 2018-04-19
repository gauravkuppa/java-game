package com.callof2d.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import main.MyGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		config.width = MyGame.WIDTH;
		config.height = MyGame.HEIGHT;
		config.resizable = false;
		
		new LwjglApplication(new MyGame(), config);
	}
}
