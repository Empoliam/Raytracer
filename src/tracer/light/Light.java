package tracer.light;

import java.awt.Color;

import patchi.math.space.Vector;

public abstract class Light {

	protected double intensity;
	protected Color color;
	
	public Light(double intensity) {
		this(intensity, Color.WHITE);
	}
	
	public Light(double intensity, Color color) {
		
		this.intensity = intensity;
		this.color = color;
		
	}
	
	public abstract double getIntensity(Vector P);
	
	public abstract Vector getDirection(Vector P);

	public abstract double getDistanceSquare(Vector P);
	
	public Color getColor() {
		return color;
	}
	
}
