package entities;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import main.AssetHandler;

public class SpawnZombie {
	Vector2[] locations;
	
	Random selector;
	
	public SpawnZombie() {
		locations = null;
		
		selector = new Random();
	}
	
	public SpawnZombie(Vector2[] spots) {
		locations = spots;
		
		selector = new Random();
	}
	
	public Zombie spawn() {
		Zombie z = null;
		if (locations != null) {
			int n = selector.nextInt(locations.length);
			z = new Zombie((int) locations[n].x, (int) locations[n].y, 211, 157);
		}
		return z;
	}
	
	public Vector2[] getSpots() {
		return locations;
	}
	
	public void setSpots(Vector2[] spots) {
		this.locations = spots;
	}
}