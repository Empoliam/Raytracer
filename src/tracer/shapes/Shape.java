package tracer.shapes;

import java.awt.Color;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.Intersect;

public abstract class Shape {

	protected Color COLOR;

	protected Shape(Color color) {
		COLOR = color;
	}
	
	public abstract Intersect intersect(Ray R, boolean cullBackface);

	public abstract Vector getNormal(Vector V);
	
	public Color getColor() {
		return COLOR;
	}
	
}
