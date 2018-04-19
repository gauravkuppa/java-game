package items;

import entities.Entity;
import main.AssetHandler;
import main.MyGame;
import map.Tile;
import map.TileMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity{
	Weapon myCurrent;
	
	float currentDistance;
	
	boolean collided;
		
	public static int regId;
	
	public Bullet() {
		setRegion(AssetHandler.getRegion(regId));
		
		setOrigin(2 / 2, 10 / 2);
		
		setX(0);
		setY(0);
		
		border = new Rectangle();
		updateBorder();
		
		
		myDelta = new Vector2(0, 0);
		
		mySpeed = 0;
	}
	
	public void init(float x, float y, Weapon current, float rotation) {
		dif = 0;
		setX(x);
		setY(y);
		
		updateBorder();
		
		myCurrent = current;
		setRotation(rotation);
		currentDistance = 0;
		collided = false;
		currentDistance = 0;
		collided = false;
	}
	
	@Override
	public boolean update(float deltaTime) {
		myDelta.x = 0;
		myDelta.y = 0;
		
		myDelta.x = -(float) Math.sin(Math.toRadians(getRotation())) * myCurrent.getBullSpeed() * deltaTime;
		myDelta.y = (float) Math.cos(Math.toRadians(getRotation())) * myCurrent.getBullSpeed() * deltaTime;
		
		currentDistance += myDelta.x + myDelta.y;
		
		if (currentDistance > myCurrent.getRange() || collided) {
			return false;
		}
		
		setX(getX() + myDelta.x);
		setY(getY() + myDelta.y);
		
		border.x = getX();
		border.y = getY();
		
		return true;
	}
	
	public float getWidth() {
		return getRegionWidth();
	}
	
	public float getHeight() {
		return getRegionHeight();
	}

	public void draw(SpriteBatch batch) {
		batch.draw(getTexture(), getX(), getY(), getOriginX(), getOriginY(), 
				getWidth(), getHeight(), 1, 1, getRotation(), 
				getRegionX(), getRegionY(), getRegionWidth(), getRegionHeight(), 
				false, false);
	}
	
	
	public boolean collideBounds(Rectangle ext) {
		boolean coll = false;
		if (isOverlaping(ext)) {
			coll = true;
			collided = true;
		}
		
		return coll;
	}
	
	public boolean collideBounds(TileMap map) {
		boolean coll = false;
		for (int i = 0; i < map.getI(); i++) {
			for (int j = 0; j < map.getJ(); j++) {
				Tile tile = map.getTile(i, j);
				if (tile != null) {
					if (this.isOverlaping(tile.getBorder()) && tile.isObstacle()) {
						coll = true;
						collided = true;
					}
				}
			}
		}
		
		return coll;
	}
	
	public Weapon getCurrentShooted() {
		return myCurrent;
	}
	
	public void setWeapon(Weapon w) {
		myCurrent = w;
	}
	
	@Override
	protected void updateBorder() {
		border.x = getX();
		border.y = getY();
		border.width = 7;
		border.height = 7;
	}
}
