package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import main.AssetHandler;
import main.MyGame;
import map.Tile;
import map.TileMap;

public abstract class Entity extends Sprite implements Disposable {

	private Animation myAnimation;

	private float sTime;

	protected float dif = 4;

	protected Rectangle border;

	protected Vector2 myDelta;

	protected float mySpeed;
	public Entity() {
		
	}
	public Entity(int animId, float x, float y, int w, int h) {
		myAnimation = AssetHandler.getAnimation(animId);

		setOrigin(w / 2, h / 2);

		setX(x);
		setY(y);

		border = new Rectangle();

		sTime = 0f;

		myDelta = new Vector2(0, 0);

		mySpeed = 0;
	}

	public void updateAnim() {
		sTime += Gdx.graphics.getDeltaTime();
	}

	public float getWidth() {
		return ((TextureRegion) myAnimation.getKeyFrame(sTime)).getRegionWidth();
	}

	public float getHeight() {
		return ((TextureRegion) myAnimation.getKeyFrame(sTime)).getRegionHeight();
	}

	public Rectangle getBorder() {
		return border;
	}

	public abstract boolean update(float deltaTime);

	public void draw(SpriteBatch batch) {
		batch.draw(getCurrentFrame(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1f, 1f,
				getRotation());
	}

	public TextureRegion getCurrentFrame() {
		return (TextureRegion) myAnimation.getKeyFrame(sTime, true);
	}

	protected void updateBorder() {
		border.x = getX() + dif;
		border.y = getY() + dif;

		border.width = getWidth() - dif * 2;
		border.height = getWidth() - dif * 2;
	}

	public boolean isOverlaping(Rectangle r) {

		boolean overlapping = true;

		updateBorder();

		if ((int) border.y + border.height <= r.y) {
			overlapping = false;
		}
		if ((int) border.y >= r.y + r.height) {
			overlapping = false;
		}
		if ((int) border.x + border.width <= r.x) {
			overlapping = false;
		}
		if ((int) border.x >= (r.x + r.width)) {
			overlapping = false;
		}

		return overlapping;
	}

	public boolean collideBounds(Rectangle r) {
		boolean collided = false;

		updateBorder();

		if (isOverlaping(r)) {
			collided = true;

			setX(getX() - myDelta.x);
			setY(getY() - myDelta.y);
			updateBorder();

			float xBefore = border.x;
			float yBefore = border.y;

			setX(getX() + myDelta.x);

			updateBorder();
			if (isOverlaping(r)) {
				if (xBefore > r.x) {
					setX(r.x + r.width - getDif());
				}
				if (xBefore < r.x) {
					setX(r.x - border.width - getDif());
				}
				updateBorder();
			}

			setY(getY() + myDelta.y);
			updateBorder();
			if (isOverlaping(r)) {
				if (yBefore < r.y) {
					setY(r.y - border.height - getDif());
				}
				if (yBefore > r.y) {
					setY(r.y + r.height - getDif());
				}
				updateBorder();
			}
		}

		return collided;
	}

	public boolean collideBounds(TileMap map) {
		boolean collided = false;
		
		setX(getX() - myDelta.x);
		setY(getY() - myDelta.y);
		
		updateBorder();
		float xBefore = border.x;
		float yBefore = border.y;
		
		setX(getX() + myDelta.x);
		updateBorder();
		for (int i = 0; i < map.getI(); i++) {
			for (int j = 0; j < map.getJ(); j++) {
				Tile tile = map.getTile(i, j);
				if (tile != null) {
					if (isOverlaping(tile.getBorder()) && tile.isObstacle()) {
						collided = true;
						if (xBefore > tile.getX()) {
							setX(tile.getBorder().x + tile.getBorder().width - getDif());
						}
						else if (xBefore < tile.getX()) {
							setX(tile.getBorder().x - border.width - getDif());
						}
						updateBorder();
					}
				}
			}
		}
		
		setY(getY() + myDelta.y);
		updateBorder();
		for (int i = 0; i < map.getI(); i++) {
			for (int j = 0; j < map.getJ(); j++) {
				Tile tile = map.getTile(i, j);
				if (tile != null) {
					if (isOverlaping(tile.getBorder()) && tile.isObstacle()) {
						collided = true;
						if (yBefore > tile.getY()) {
							setY(tile.getBorder().y + tile.getBorder().height - getDif());
						}
						else if (yBefore < tile.getY()) {
							setY(tile.getBorder().y - border.height - getDif());
						}
						updateBorder();
					}
				}
			}
		}
		
		return collided;
	}

	public float getDif() {
		return dif;
	}

	@Override
	public void dispose() {
		getTexture().dispose();
	}

	public void setAnimation(Animation anim) {
		myAnimation = anim;
	}

	public Animation getAnimation() {
		return myAnimation;
	}

}
