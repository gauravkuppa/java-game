package main;

public class SinCos {
	private static float[] cosine = new float[360];
	private static float[] sine = new float[360];
	
	public static void init() {
		for	(int i = 0; i < 360; i++) {
			sine[i] = (float) Math.sin(Math.toRadians(i));
			cosine[i] = (float) Math.cos(Math.toRadians(i));			
		}
	}
	
	public static float getCosine(int angle) {
		while (angle >= 360)
			angle -= 360;
		while (angle < 0)
			angle += 360;
		return cosine[angle];
	}
	
	public static float getSine(int angle) {
		while (angle >= 360)
			angle -= 360;
		while (angle < 0)
			angle += 360;
		return sine[angle];
	}
	
}