package tracer.shapes;

import java.awt.Color;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.Intersect;

public class Tri extends Shape {
	
	private final Vector A;
	private final Vector AB;
	private final Vector AC;
	
	private Vector normal;
	
	public Tri(Vector A, Vector B, Vector C, Color color) {
				
		super(color);
		
		this.A = A;
		AB = new Vector(A, B);
		AC = new Vector(A, C);
		
		normal = AB.cross(AC).normalize();
						
	}

	public void flipNormal() {
		this.normal = this.normal.negate();
	}
	
	@Override
	public Intersect intersect(Ray R) {
		
		Vector dN = R.getDirection().negate();
		Vector O = R.getOrigin();	
				
		Vector AO = new Vector(A,O);
		
		boolean backface = false;
		
		double modM = dN.dot(AB.cross(AC));
		if(modM < 0) backface = true;
		
		double t = AO.dot(AB.cross(AC)) / modM;
		if(t < 0) return null;
		
		double u = dN.dot(AO.cross(AC)) / modM;
		if(u < 0 || u > 1) return null;
		
		double v = dN.dot(AB.cross(AO)) / modM;
		if(v < 0 || u > 1 || u+v > 1) return null;
		
		return new Intersect(t, R.getPointFromParameter(t), this, R, backface);
		
	}

	@Override
	public Vector getNormal(Vector V) {
		return normal;
	}

}
