package map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import entities.Entity;
import main.AssetHandler;

public class Tile extends Entity {
	private boolean isObstacle;

	public Tile(int regId, float dx, float dy, int width, int height, boolean isOb) {
		isObstacle = isOb;
		dif = 0;

		setRegion(AssetHandler.getRegion(regId));

		border = new Rectangle(dx, dy, width, height);
		setX(dx);
		setY(dy);
	}

	@Override
	public float getWidth() {
		return getRegionWidth();
	}

	@Override
	public float getHeight() {
		return getRegionHeight();
	}

	@Override
	public boolean update(float deltaTime) {
		return true;
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(getTexture(), getX(), getY(), getWidth(), getHeight(), getRegionX(),
				getRegionY(), getRegionWidth(), getRegionHeight(), false, false);
	}

	@Override
	public boolean collideBounds(Rectangle ext) {
		boolean coll = isOverlaping(ext);

		return coll;
	}

	public boolean isObstacle() {
		return isObstacle;
	}

}
