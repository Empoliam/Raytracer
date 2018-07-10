package tracer.shapes;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.Intersect;

public class Sphere extends Shape {

	double radius;
	Vector centre;

	public Sphere(double radius, Vector centre) {

		this.radius = radius;
		this.centre = centre;
	}

	@Override
	public Intersect intersect(Ray R) {
	
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
		
		return new Intersect(t,R.getPointFromParameter(t),this);
		
	}

	@Override
	public Vector getNormal(Vector V) {

		return new Vector(centre, V).normalize();
		
	}
	
}
