package tracer.light;

import patchi.math.space.Vector;

public abstract class Light {

	protected double intensity;
	
	public Light(double intensity) {
		
		this.intensity = intensity;
		
	}
	
	public abstract double getIntensity(Vector P);
	
	public abstract Vector getDirection(Vector P);

	public abstract double getDistanceSquare(Vector P);
	
}
