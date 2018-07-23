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
	
	public Intersect(double t, Vector coords, Shape S, Ray inbound, Vector normal) {
		
		this.t = t;
		this.coords=coords;
		this.inbound = inbound;
		this.shape = S;
		this.normal = normal;
		
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

}
