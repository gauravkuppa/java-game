package main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import screens.GameScreen;
import screens.MainMenu;

public class MyGame extends Game {
	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;
	
	
	public SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render() {	
		super.render();
	}

	@Override
	public void dispose() {
		
	}
}
