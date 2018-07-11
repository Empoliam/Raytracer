package tracer.shapes;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.Intersect;

public class Tri extends Shape {
	
	private final Vector A;
	private final Vector AB;
	private final Vector AC;
	
	private final Vector normal;
	
	public Tri(Vector A, Vector B, Vector C) {
				
		this.A = A;
		AB = new Vector(A, B);
		AC = new Vector(A, C);
		
		normal = AB.cross(AC).normalize();
						
	}

	@Override
	public Intersect intersect(Ray R) {
		
		Vector d = R.getDirection();
		Vector O = R.getOrigin();	
		Vector AO = new Vector(A,O);
		
		double modM = d.negate().dot(AB.cross(AC));
		
		double t = AO.dot(AB.cross(AC)) / modM;
		if(t < 0) return null;
		
		double u = d.negate().dot(AO.cross(AC)) / modM;
		if(u < 0 || u > 1) return null;
		
		double v = d.negate().dot(AB.cross(AO)) / modM;
		if(v < 0 || u > 1 || u+v > 1) return null;
		
		return new Intersect(t, R.getPointFromParameter(t), this, R);
		
	}

	@Override
	public Vector getNormal(Vector V) {
		return normal;
	}

}
