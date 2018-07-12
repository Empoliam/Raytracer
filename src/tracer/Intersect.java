package tracer;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.shapes.Shape;

public class Intersect {

	private final double t;
	private final Vector coords;
	private final Vector normal;
	private final Ray inbound; 
	private final Shape shape;
	private final boolean backface;
	
	public Intersect(double t, Vector coords, Shape S, Ray inbound, boolean backface) {
		
		this.t = t;
		this.coords=coords;
		this.inbound = inbound;
		this.shape = S;
		normal = S.getNormal(coords);
		this.backface = backface;
		
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

	public Ray getInbound() {
		return inbound;
	}

	public Shape getShape() {
		return shape;
	}

	public boolean getBackface() {
		return backface;
	}

}
