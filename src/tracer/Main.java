package tracer;

import java.awt.Color;

import patchi.math.space.Vector;
import tracer.light.PointLight;
import tracer.shader.DiffuseShader;
import tracer.shader.Material;
import tracer.shader.ReflectionShader;
import tracer.shapes.Plane;
import tracer.shapes.Polyhedron;

public class Main {

	private static final Vector CAMERA_ORIGIN = new Vector(0d,0d,5d);
	private static final double CAMERA_PITCH = 0d;
	private static final double CAMERA_YAW = 0d;
	private static final double CAMERA_ROLL = 0d;
	private static final int XRES = 1920;
	private static final int YRES = 1080;
	private static final double FOV = 50d;
	private static final boolean AA = true;
	private static final int THREADS = -1;
	private static final int TILE_SIZE = 256;
	private static final double BIAS = 1e-8;

	public static void main(String[] args) {

		Raytracer R = new Raytracer(CAMERA_ORIGIN, CAMERA_PITCH, CAMERA_YAW, CAMERA_ROLL, XRES, YRES, FOV, AA, THREADS, TILE_SIZE, BIAS);
		
		R.addLight(new PointLight(new Vector(0.7d,0.5d,0.7d), 100d, Color.MAGENTA));
		R.addLight(new PointLight(new Vector(-0.7d,-0.5d,-0.7d), 100d, Color.YELLOW));

		Material CUBE = new Material(new DiffuseShader(Color.RED, 0.5d, R));
		AffineMatrix cubeMatrix = AffineMatrix.buildMatrix(0d, 0d, 0d, new Vector(0d,-0.5d,0d));
		Polyhedron C = Polyhedron.buildCube(CUBE, cubeMatrix, 0.5);
		
		Material MIRROR = new Material(new ReflectionShader(Color.WHITE,1d,R));
		Material FLOOR = new Material(new DiffuseShader(Color.WHITE, 0.18, R));
		
		AffineMatrix bMatrix = AffineMatrix.buildMatrix(90d, 0d, 0d, new Vector(0d,0d,-1d));
		Plane rear = new Plane(MIRROR, bMatrix);
		AffineMatrix lMatrix = AffineMatrix.buildMatrix(0d, 0d, -90d, new Vector(-1d,0d,0d));
		Plane left = new Plane(MIRROR, lMatrix);
		AffineMatrix rMatrix = AffineMatrix.buildMatrix(0d, 0d, 90d, new Vector(1d,0d,0d));
		Plane right = new Plane(MIRROR, rMatrix);
		AffineMatrix nMatrix = AffineMatrix.buildMatrix(-90d, 0d, 0d, new Vector(0d,0d,1d));
		Plane near = new Plane(MIRROR, nMatrix);
		
		AffineMatrix fMatrix = AffineMatrix.buildMatrix(0d, 0d, 0d, new Vector(0d,-1d,0d));
		Plane floor = new Plane(FLOOR, fMatrix);
		AffineMatrix cMatrix = AffineMatrix.buildMatrix(0d, 0d, 180d, new Vector(0d,1d,0d));
		Plane ceiling = new Plane(FLOOR, cMatrix);
		
		R.addShape(C);
		
		R.addShape(rear);
		R.addShape(left);
		R.addShape(right);
		R.addShape(near);
		
		R.addShape(floor);
		R.addShape(ceiling);
		
		R.write();

	}

}
