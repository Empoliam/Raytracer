package tracer.shapes;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.AffineMatrix;
import tracer.Intersect;
import tracer.shader.Material;

public class Sphere extends Shape {

	double radius;
	Vector centre;

	public Sphere(double radius, AffineMatrix M, Material mat) {

		super(mat, M);
		
		this.radius = radius;
		this.centre = M.transform(new Vector(0d,0d,0d));
		
	}

	@Override
	public Intersect intersect(Ray R, boolean cullBackface) {
	
		Vector dir = R.getDirection();
		Vector rayor = new Vector(centre,R.getOrigin());
		
		double b = 2d * dir.dot(rayor);
		double c = rayor.dot(rayor) - (radius*radius);
				
		double discriminant = (b*b) - 4d * (c);
		
		double t = -1d;
		
		if(discriminant < 0d) {
			return null;
		} else if (discriminant == 0d) {
			t = (-b) * 0.5d;
		}
		else {
			
			double t1 = (-b + Math.sqrt(discriminant)) * 0.5d;
			double t2 = (-b - Math.sqrt(discriminant)) * 0.5d;
			
			t = t1;
			
			if(t1 < 0d && t2 < 0d) {
				return null;
			} else {
				if(t2 < t1) t = t2;
			}
			
		}
		
		Vector O = R.getPointFromParameter(t);
		
		return new Intersect(t, O, this, R, getNormal(O));
		
	}

	public Vector getNormal(Vector V) {

		return new Vector(centre, V).normalize();
		
	}
	
}
