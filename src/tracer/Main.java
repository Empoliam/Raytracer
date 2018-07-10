package tracer;

import patchi.math.space.Vector;
import tracer.shapes.Sphere;

public class Main {

	public static void main(String[] args) {
				
		Raytracer R = new Raytracer(new Vector(0d,0d,5d), 0, 0, 0, 1280, 720, 1d, 60d, true);
		
		R.addShape(new Sphere(1d,new Vector(0d,0d,0d)));
		R.addShape(new Sphere(0.3d, new Vector(1d,0.2d,0.5d)));
		
		R.write();
		
	}

}
