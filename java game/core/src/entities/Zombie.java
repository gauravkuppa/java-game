package entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;

import main.MyGame;
import main.SinCos;
import map.TileMap;

public class Zombie extends Entity{
	int myDamage;
	Vector2 myObjective;
	long myLastDamaged;
	int[] placesX = new int[200];
	int[] placesY = new int[200];
	
	float myCosine;
	float mySine;
	TileMap myMap;
	float myHP;
	public static int myAnimationID;
	boolean chaseObj;
	
	public Zombie() {
		super(myAnimationID, 0, 0, 60, 60);
	}
	
	public Zombie(int x, int y, int width, int height) {
		super(myAnimationID, x, y, width, height);
		dif = 4;
		updateBorder();
		myObjective = new Vector2(0, 0);
		myDamage = 1;
		mySpeed = 150;
		myHP = 100;
		myLastDamaged = 0;
		chaseObj = true;
		
		for (int i = 0; i <= 99; i++) {
			placesX[i] = i - 99;
			placesY[i] = i - 99;
		}
		
		for (int i = 100; i <= 199; i++) {
			placesX[i] = i + MyGame.WIDTH - 100;
			placesY[i] = i + MyGame.HEIGHT - 100;
		}
	}
	
	public void init(TileMap m) {
		dif = 4;
		updateBorder();
		myObjective = new Vector2(0, 0);
		myDamage = 1;
		mySpeed = 150;
		myHP = 100;
		myMap = m;
		myLastDamaged = 0;
		float x;
		float y;
		
		Random rand = new Random();
		do {
			x = rand.nextInt(myMap.getJ() - 2) * 60;
			y = rand.nextInt(myMap.getI() - 2) * -60;
		} while (myMap.getTileOf(x, y) == null || myMap.getTileOf(x, y).isObstacle());

		setX(x);
		setY(y);
	}
	
	@Override
	public boolean update(float deltaTime) {
		myDelta.x = 0;
		myDelta.y = 0;
		
		if (this.myHP <= 0)
			return false;

		if (myObjective != null && chaseObj) {
			
			Vector2 dir = new Vector2(myObjective.x - getX(), myObjective.y - getY());
			dir.rotate90(-1);
			
			this.setRotation(dir.angle());
			
			mySine = SinCos.getSine((int)getRotation());
			myCosine = SinCos.getCosine((int)getRotation());
			
			
			myDelta.x = -mySine * mySpeed * deltaTime;
			myDelta.y = myCosine * mySpeed * deltaTime;
			
			setX(getX() + myDelta.x);
			setY(getY() + myDelta.y);
		}
		
		if(myDelta.x != 0 || myDelta.y != 0)
			updateAnim();
		
		updateBorder();
		
		return true;
	}
	
	public void setObjective(float x, float y) {
		if (myMap != null) {
			myObjective.x = x;
			myObjective.y = y;
		}
	}
	
	public void setObjective(Vector2 obj) {
		if (obj != null)
			setObjective(obj.x, obj.y);
		else 
			myObjective = obj;
	}
	
	public void setMap(TileMap m) {
		myMap = m;
	}
	public void damage(int d) {
		float dTime = Gdx.graphics.getDeltaTime();
		if ((System.nanoTime() - myLastDamaged) / 1000000 > 200)
		{
			myLastDamaged = System.nanoTime();
		}
		this.myHP -= d;
	}
	
	public int getDamage() {
		return myDamage;
	}
	
	public boolean alive() {
		return myHP > 0;
	}
	
	public float getCosine() {
		return myCosine;
	}
	
	public float getSine() {
		return mySine;
	}

	public void forceStop() {
		chaseObj = false;
	}
	public void forceStart() {
		chaseObj = true;
	}
}
