package tracer;

import java.awt.Color;

import patchi.math.space.Vector;
import tracer.shader.DiffuseShader;
import tracer.shader.Material;
import tracer.shapes.Face;
import tracer.shapes.Sphere;

public class Main {

	private static final Vector CAMERA_ORIGIN = new Vector(0d,1d,3d);
	private static final double CAMERA_PITCH = -10d;
	private static final double CAMERA_YAW = 0d;
	private static final double CAMERA_ROLL = 0d;
	private static final int XRES = 1920;
	private static final int YRES = 1080;
	private static final double FOV = 50d;
	private static final boolean AA = true;
	private static final int THREADS = -1;
	private static final int TILE_SIZE = 256;
	
	public static void main(String[] args) {
				
		Raytracer R = new Raytracer(CAMERA_ORIGIN, CAMERA_PITCH, CAMERA_YAW, CAMERA_ROLL, XRES, YRES, FOV, AA, THREADS, TILE_SIZE);
		
		R.addLight(new tracer.light.PointLight(new Vector(2d,2d,-2d), 1360d));
		R.addLight(new tracer.light.PointLight(CAMERA_ORIGIN, 50d));
		
		AffineMatrix O = AffineMatrix.buildMatrix(0d, 0d, 0d, new Vector(0d,0d,0d));
		AffineMatrix M = AffineMatrix.buildMatrix(0d, 25d, 0d, new Vector(0d,0.25d,0d));
		AffineMatrix S = AffineMatrix.buildMatrix(0d, 0d, 0d, new Vector(-1d,0.25d,-1d));
				
		Material WHITE = new Material(new DiffuseShader(Color.WHITE,0.18d,R,R.getLights(),1e-8));
		Material CYAN = new Material(new DiffuseShader(Color.CYAN,0.5d,R,R.getLights(),1e-8));
		Material RED = new Material(new DiffuseShader(Color.RED,0.5d,R,R.getLights(),1e-8));
				
		R.addShape(new Face(WHITE, O, new Vector(20d,0d,20d), new Vector(20d,0d,-20d), new Vector(-20d,0d,-20d), new Vector(-20d,0d,20d)));
		
		R.addShape(new Face(CYAN, M, new Vector(0.25d,0.25d,0.25d), new Vector(-0.25d,0.25d,0.25d), new Vector(-0.25d,-0.25d,0.25d), new Vector(0.25d,-0.25d,0.25d)));
		R.addShape(new Face(CYAN, M, new Vector(0.25d,0.25d,-0.25d), new Vector(0.25d,-0.25d,-0.25d), new Vector(-0.25d,-0.25d,-0.25d), new Vector(-0.25d,0.25d,-0.25d)));
		R.addShape(new Face(CYAN, M, new Vector(0.25d,0.25d,0.25d), new Vector(0.25d,-0.25d,0.25d), new Vector(0.25d,-0.25d,-0.25d), new Vector(0.25d,0.25d,-0.25d)));
		R.addShape(new Face(CYAN, M, new Vector(0.25d,0.25d,0.25d), new Vector(0.25d,0.25d,-0.25d), new Vector(-0.25d,0.25d,-0.25d), new Vector(-0.25d,0.25d,0.25d)));
		R.addShape(new Face(CYAN, M, new Vector(-0.25d,0.25d,0.25d), new Vector(-0.25d,0.25d,-0.25d), new Vector(-0.25d,-0.25d,-0.25d), new Vector(-0.25d,-0.25d,0.25d)));
		R.addShape(new Face(CYAN, M, new Vector(0.25d,-0.25d,0.25d), new Vector(-0.25d,-0.25d,0.25d), new Vector(-0.25d,-0.25d,-0.25d), new Vector(0.25d,-0.25d,-0.25d)));
				
		R.addShape(new Sphere(0.25, S, RED));
		
		R.write();
		
	}

}
