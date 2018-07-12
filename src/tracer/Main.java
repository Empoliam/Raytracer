package tracer;

import java.awt.Color;

import patchi.math.space.Vector;
import tracer.shapes.Sphere;
import tracer.shapes.Tri;

public class Main {

	private static final Vector CAMERA_ORIGIN = new Vector(1.15d,0.5d,1.5d);
	private static final double CAMERA_PITCH = -10d;
	private static final double CAMERA_YAW = 23d;
	private static final double CAMERA_ROLL = 0d;
	private static final int XRES = 1000;
	private static final int YRES = 1000;
	private static final double FOCAL_LENGTH = 1d;
	private static final double FOV = 50d;
	private static final boolean AA = true;
	private static final int THREADS = -1;
	private static final int TILE_SIZE = 256;
	
	public static void main(String[] args) {
				
		Raytracer R = new Raytracer(CAMERA_ORIGIN, CAMERA_PITCH, CAMERA_YAW, CAMERA_ROLL, XRES, YRES, FOCAL_LENGTH, FOV, AA, THREADS, TILE_SIZE);
				
		R.addShape(new Tri(new Vector(0d,0d,-2d), new Vector(1d,1d,-1d), new Vector(-1d, 1d,-1d), Color.RED));
		R.addShape(new Tri(new Vector(0d,0d,-2d), new Vector(1d,-1d,-1d), new Vector(1d,1d,-1d), Color.WHITE));
		R.addShape(new Tri(new Vector(0d,0d,-2d), new Vector(-1d,-1d,-1d), new Vector(1d,-1d,-1d), Color.RED));
		R.addShape(new Tri(new Vector(0d,0d,-2d), new Vector(-1d,1d,-1d), new Vector(-1d,-1d,-1d), Color.WHITE));
		
		R.addShape(new Sphere(0.3d, new Vector(0d,0d,0d), Color.CYAN));
		
		R.addLight(new tracer.light.PointLight(new Vector(-0.5d,0.5d,2d), 1d));
		
		R.write();
		
	}

}
