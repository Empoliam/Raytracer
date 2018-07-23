package tracer.light;

import java.awt.Color;

import patchi.math.space.Vector;

public class PointLight extends Light {

	private Vector origin;

	public PointLight(Vector O, double intensity) {

		super(intensity);	

		this.origin = O;

	}

	public PointLight(Vector O, double intensity, Color C) {

		super(intensity,C);	

		this.origin = O;

	}

	@Override
	public Vector getDirection(Vector P) {	
		return new Vector(P,origin).normalize();
	}

	@Override
	public double getIntensity(Vector P) {

		Vector V = new Vector(P,origin);
		double rsquare = V.dot(V);
		return super.intensity / (4d * Math.PI * rsquare);

	}

	@Override
	public double getDistanceSquare(Vector P) {
		Vector V = new Vector(P, origin);
		return V.dot(V);
	}

}
