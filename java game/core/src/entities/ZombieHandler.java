package entities;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Pool;

import map.TileMap;

public class ZombieHandler {
	public ArrayList<Zombie> myZombies;
	private Pool<Zombie> myZombiesPool;
	
	private long lastSpawned;
	private int delay;

	private TileMap myMap;
	private int myCount;
	
	public ZombieHandler(TileMap m) {
		
		myZombies = new ArrayList<Zombie>();
		
		myZombiesPool = new Pool<Zombie>() {
			public Zombie newObject() {
				return new Zombie();
			}
		};
		
		lastSpawned = 0;
		
		myMap = m;
		delay = 1500;
	}
	
	public boolean spawn() {
		if ((System.nanoTime() - lastSpawned) / 1000000 > delay) {
			
			lastSpawned = System.nanoTime();
			Zombie z = myZombiesPool.obtain();
			
			z.init(myMap);
			myZombies.add(z);
			
			myCount++;
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isEmpty() {
		return myZombies.isEmpty();
	}
	
	public int getCount() {
		return myCount;
	}
	
	public void resetCount() {
		myCount = 0;
	}
	
	public void remove(Zombie z) {
		myZombiesPool.free(z);
		
		myZombies.remove(z);
	}

	public void forceSpawn() {
		lastSpawned = 0;
		spawn();
	}
}
