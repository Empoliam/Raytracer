package tracer.light;

import patchi.math.space.Vector;

public class PointLight extends Light {
	
	private Vector origin;
	
	public PointLight(Vector O, double intensity) {
		
		super(intensity);	
		
		this.origin = O;
		
	}

	@Override
	public Vector getDirection(Vector P) {	
		return new Vector(P,origin).normalize();
	}

	@Override
	public double getIntensity(Vector P) {
		return super.intensity;
	}

	@Override
	public double getDistanceSquare(Vector P) {
		Vector V = new Vector(P, origin);
		return V.dot(V);
	}
	
		

}
