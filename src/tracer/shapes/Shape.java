package tracer.shapes;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.Intersect;

public abstract class Shape {

	public abstract Intersect intersect(Ray R);

	public abstract Vector getNormal(Vector V);
	
}
