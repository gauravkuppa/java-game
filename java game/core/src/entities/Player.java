package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import items.Weapon;
import main.AssetHandler;
import main.MyGame;
import main.SinCos;

public class Player extends Entity {
	public static final int PLAYERSIZE = 112;

	private int hp;
	private int ammo;

	private Weapon current;
	private Weapon knife;
	private Weapon pistol;
	private Weapon rifle;

	private int score;
	private long lastDamaged;
	private long damageDelay;

	private Vector2 velocity = new Vector2();
	private final int SPEED = 500;
	float angle;

	public Player(int animId, float x, float y, int width, int height) {
		super(animId, x, y, width, height);

		dif = 4;

		hp = 100;

		ammo = 1000000;

		pistol = new Weapon(AssetHandler.createAnimation("player.png", 20, 0, 0, 75, 101), 130, 10, 1000, 1000, false);
		knife = new Weapon(AssetHandler.createAnimation("player.png", 20, 0, 0, 75, 101), 30, 3, 50, 0, true);
		knife.setBorder(border);
		current = pistol;

		mySpeed = 270;

		score = 0;
		lastDamaged = 0;
		damageDelay = 600;
		updateBorder();
	}

	public void draw(SpriteBatch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	public boolean update(float deltaTime) {
		
		float centerX = Gdx.graphics.getWidth() / 2;
		float centerY = Gdx.graphics.getHeight() / 2;
		
		float mouseX = Gdx.input.getX();
		float mouseY = Gdx.input.getY();
		
		Vector2 direction = new Vector2(mouseX - centerX, mouseY - centerY);
		//direction.rotate90(-1);
		setRotation(direction.angle());

		if (Gdx.input.isKeyPressed(Keys.S)) {
			myDelta.y = -mySpeed * deltaTime;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			myDelta.y = mySpeed * deltaTime;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			myDelta.x = mySpeed * deltaTime;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			myDelta.x = -mySpeed * deltaTime;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.W) || 
				Gdx.input.isKeyJustPressed(Keys.S)) {
			myDelta.y = 0;
		}
		if (Gdx.input.isKeyJustPressed(Keys.A) ||
				Gdx.input.isKeyJustPressed(Keys.D)) {
			myDelta.x = 0;
		}

		if (myDelta.x != 0 || myDelta.y != 0) {
			updateAnim();
		}
		
		setX(getX() + myDelta.x);
		setY(getY() + myDelta.y);
		
		if (Gdx.input.isTouched()) {
			float tX = getX() + (getWidth() / 2);
			float tY = getY() + (getHeight() / 2);

			tX -= SinCos.getSine((int) getRotation() + 20) * 23;
			tY += SinCos.getCosine((int) getRotation() + 20) * 23;

			if (!current.isMeele()) {
				if (ammo > 0) {
					ammo--;
					current.shoot(tX, tY, getRotation());
				}
			} else {
				current.shoot(tX, tY, getRotation());
			}
		}
		return false;
	}

	public int getAmmo() {
		return ammo;
	}

	public void addAmmo(int amount) {
		ammo += amount;
	}

	public int getHp() {
		return hp;
	}

	public void damage(int amount, float xDir, float yDir) {
		if ((System.nanoTime() - lastDamaged) / 1000000 > damageDelay) {
			lastDamaged = System.nanoTime();

			hp -= amount;
		}
	}

	public boolean alive() {
		return hp > 0;
	}

	public void addScore(int a) {
		score += a;
	}

	public int getScore() {
		return score;
	}

	public void setWeapon(Weapon w) {
		current = w;
	}

	public Weapon getWeapon() {
		return current;
	}

	public void heal(int am) {
		hp += am;
	}

	public boolean isDamaged() {
		return ((System.nanoTime() - lastDamaged) / 1000000 < damageDelay) ? true : false;
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
