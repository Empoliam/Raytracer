package tracer;

import patchi.math.space.Vector;
import tracer.shapes.Sphere;
import tracer.shapes.Tri;

public class Main {

	public static void main(String[] args) {
				
		Raytracer R = new Raytracer(new Vector(0d,0d,3d), 0d, 0d, 0d, 5000, 5000, 1d, 30d, true);
		
//		R.addShape(new Sphere(0.5d, new Vector(0d,0d,0d)));
//		R.addShape(new Sphere(0.3d, new Vector(1d,0.2d,0.5d)));
		
		R.addShape(new Tri(new Vector(-0.5d,0.5d,0.5d), new Vector(0d,-0.5d,0d), new Vector(0.5d,0.5d,0d)));
		
		R.addShape(new Sphere(0.025d, new Vector(-0.5d,0.5d,0.5d)));
		R.addShape(new Sphere(0.025d, new Vector(0d,-0.5d,0d)));
		R.addShape(new Sphere(0.025d, new Vector(0.5d,0.5d,0d)));
		
		R.write();
		
	}

}
