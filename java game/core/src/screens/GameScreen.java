package screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Player;
import entities.Zombie;
import entities.ZombieHandler;
import items.Bullet;
import main.AssetHandler;
import main.MyGame;
import main.SinCos;
import map.TileMap;

public class GameScreen implements Screen {	
	OrthographicCamera cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	SpriteBatch batch;
	BitmapFont font;
	
	Player player;
	ArrayList<Bullet> bullets;
	
	ZombieHandler myZombieHandler;
	
	int wave;
	TileMap map;
	MyGame game;
	
	public GameScreen(MyGame g) {
		game = g;
	}
	@Override
	public void show() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		SinCos.init();
		AssetHandler.initAssetHandler();
		
		Zombie.myAnimationID = AssetHandler.createAnimation("player.png", 20, 0, 0, 75, 101);
		Bullet.regId = AssetHandler.createRegion("atlas.png", 256, 0, 6, 13);
		player = new Player(AssetHandler.createAnimation("player.png", 20, 0, 0, 75, 101),
				62, -58, 60, 60);
		
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.setToOrtho(false);
		
		int[][] mapSpec = {
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
				{1, 2, 1, 1, 1, 2, 2, 2, 2, 1, 2, 1},
				{1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1},
				{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
				{1, 2, 2, 2, 1, 2, 1, 1, 1, 2, 2, 1},
				{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
				{1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1},
				{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
				{1, 1, 2, 2, 2, 1, 2, 1, 2, 2, 2, 1},
				{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
				{1, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1},
				{1, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		};

		map = new TileMap(mapSpec);
		myZombieHandler = new ZombieHandler(map);
		wave = 1;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.position.x = player.getX() + (player.getWidth() / 2);
		cam.position.y = player.getY() + (player.getHeight() / 2);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		System.out.println(cam.position.x);
		System.out.println(cam.position.y);
		
		if (player.alive()) {
			if (myZombieHandler.getCount() < wave * 5) {
				myZombieHandler.spawn(); 
			}
		}
		
		if (myZombieHandler.isEmpty()) {
			wave++;
			myZombieHandler.resetCount();
			myZombieHandler.forceSpawn();
		}

		
		//Movement
		for (int j = 0; j < myZombieHandler.myZombies.size(); j++) {
			if (myZombieHandler.myZombies.get(j).alive()) {
				if (player.alive()) 
					myZombieHandler.myZombies.get(j).setObjective(player.getBorder().x, player.getBorder().y);
				else
					myZombieHandler.myZombies.get(j).setObjective(null);
				myZombieHandler.myZombies.get(j).update(delta);
			}
			else {
				
				//is.spawn(myZombieHandler.myZombies.get(j).getX(), myZombieHandler.myZombies.get(j).getY());
				myZombieHandler.remove(myZombieHandler.myZombies.get(j));
				player.addScore(10);
			}
		}
		
		if (player.alive()) {
			player.update(delta);
		}
		bullets = player.getWeapon().getBullets();
		player.getWeapon().update(myZombieHandler.myZombies, delta);
		
		
		//*******************
		// Collision
		//*******************
		
		
		/*
		for (int j = 0; j < is.items.size(); j++) {
			if (is.items.get(j).isOverlaping(player.getAABB())) {
				is.items.get(j).pickUp(player);
				is.delete(is.items.get(j));
			}
		}
		*/
		
		
		for (int j = 0; j < myZombieHandler.myZombies.size(); j++) {
			if (myZombieHandler.myZombies.get(j).alive()) {
				map.collideBounds(myZombieHandler.myZombies.get(j));
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			if (bullets.get(i).collideBounds(map)) {
				bullets.remove(i);
			}
		}
		
		for (int j = 0; j < myZombieHandler.myZombies.size(); j++) {
			if (myZombieHandler.myZombies.get(j).alive()) {
				if (myZombieHandler.myZombies.get(j).isOverlaping(player.getBorder()) && player.alive()) {
					myZombieHandler.myZombies.get(j).forceStop();
					player.damage(1, -myZombieHandler.myZombies.get(j).getSine(), myZombieHandler.myZombies.get(j).getCosine());
				}
				else {
					myZombieHandler.myZombies.get(j).forceStart();
				}
				
				for (int i = 0; i < bullets.size(); i++) {
					if (bullets.get(i).collideBounds(myZombieHandler.myZombies.get(j).getBorder()))
						myZombieHandler.myZombies.get(j).damage(bullets.get(i).getCurrentShooted().getDamage());
				}
			}
		}
		map.collideBounds(player);
		//*******************
		// Drawing
		//*******************
		batch.begin();
		map.draw(batch);
		
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(batch);
		}		
		
		if (player.isDamaged())
			batch.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		if (player.alive())
			player.draw(batch);
		batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		for (int j = 0; j < myZombieHandler.myZombies.size(); j++) {
			if (myZombieHandler.myZombies.get(j).alive()) {
				myZombieHandler.myZombies.get(j).draw(batch);
			}
		}
		
		font.draw(batch, "Wave: " + wave, 330, 120);
		font.draw(batch, "Player:", cam.position.x - 300, cam.position.y + 230);
		font.draw(batch, "Life " + player.getHp(), cam.position.x - 300, cam.position.y + 200);
		font.draw(batch, "Ammo " + player.getAmmo(), cam.position.x - 300, cam.position.y + 170);
		font.draw(batch, "Score " + player.getScore(), cam.position.x - 300, cam.position.y + 140);
		
		font.draw(batch, "Weapon:", cam.position.x - 300, cam.position.y - 140);
		font.draw(batch, "Damage: " + player.getWeapon().getDamage(), cam.position.x - 300, cam.position.y - 170);
		font.draw(batch, "Rate: " + player.getWeapon().getFireRate(), cam.position.x - 300, cam.position.y - 200);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		game.batch.dispose();
	}

}
