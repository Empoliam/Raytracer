package tracer;

import patchi.math.space.Vector;
import tracer.shapes.Shape;

public class Intersect {

	private final double t;
	private final Vector coords;
	private final Vector normal;
	
	public Intersect(double t, Vector coords, Shape S) {
		
		this.t = t;
		this.coords=coords;
		
		normal = S.getNormal(coords);
		
	}

	public double getT() {
		return t;
	}

	public Vector getCoords() {
		return coords;
	}

	public Vector getNormal() {
		return normal;
	}

}
