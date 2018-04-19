package items;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import entities.Zombie;
import main.AssetHandler;

public class Weapon {
	int myFireRate;
	int myDamage;
	int myRange;
	int myBulletSpeed;
	int myAnimationID;

	boolean isMelee;
	boolean isHitting;

	Rectangle border;

	long lastShot;

	float state;

	ArrayList<Bullet> bullets;
	Pool<Bullet> bulletPool;

	public Weapon(int animId, int fRate, int dam, int range, int bSpeed, boolean isM) {
		myAnimationID = animId;
		myFireRate = fRate;
		myDamage = dam;
		myRange = range;
		myBulletSpeed = bSpeed;
		isMelee = isM;
		lastShot = 0;
		isHitting = false;
		state = 0;

		bullets = new ArrayList<Bullet>();

		bulletPool = new Pool<Bullet>() {
			public Bullet newObject() {
				return new Bullet();
			}
		};
	}

	public void update(ArrayList<Zombie> zombies, float deltaTime) {
		if (isHitting && isMelee) {
			state += deltaTime;
			for (int i = 0; i < zombies.size(); i++) {
				if (zombies.get(i).isOverlaping(border)) {
					zombies.get(i).damage(myDamage);
				}
			}
		}
		if (!isMelee) {
			for (int i = 0; i < bullets.size(); i++) {
				boolean alive = bullets.get(i).update(deltaTime);
				if (!alive) {
					bulletPool.free(bullets.get(i));
					bullets.remove(i);
				}
			}
		}
		if ((System.nanoTime() - lastShot) / 1000000 > 250 && isMelee) {
			isHitting = false;
		}
	}

	public void draw(SpriteBatch batch, float x, float y, float rotation) {
		batch.draw((TextureRegion) AssetHandler.getAnimation(myAnimationID).getKeyFrame(state, true), x, y, 30, 30, 60,
				60, 1, 1, rotation);
	}

	public void shoot(float x, float y, float angle) {
		if ((System.nanoTime() - lastShot) / 1000000 > myFireRate) {
			lastShot = System.nanoTime();

			if (!isMelee) {
				Bullet b = bulletPool.obtain();
				b.init(x, y, this, angle);
				bullets.add(b);
			} else {
				isHitting = true;
			}
		}
	}

	public int getDamage() {
		return myDamage;
	}

	public int getFireRate() {
		return myFireRate;
	}

	public int getRange() {
		return myRange;
	}

	public int getBullSpeed() {
		return myBulletSpeed;
	}

	public int getAnimationId() {
		return myAnimationID;
	}

	public void setBorder(Rectangle b) {
		border = b;
	}

	public Rectangle getBorder() {
		return border;
	}

	public boolean isMeele() {
		return isMelee;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}