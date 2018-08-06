package tracer;

import java.awt.Color;
import java.io.File;

import patchi.math.space.Vector;
import tracer.light.PointLight;
import tracer.shader.DiffuseShader;
import tracer.shader.Material;
import tracer.shader.ReflectionShader;
import tracer.shapes.Plane;
import tracer.shapes.Polyhedron;

public class Main {

	private static final Vector CAMERA_ORIGIN = new Vector(0d,1.5d,7d);
	private static final double CAMERA_PITCH = -10d;
	private static final double CAMERA_YAW = 0d;
	private static final double CAMERA_ROLL = 0d;
	private static final int XRES = 128*2;
	private static final int YRES = 72*2;
	private static final double FOV = 50d;
	private static final boolean AA = false;
	private static final int THREADS = -1;
	private static final int TILE_SIZE = 256;
	private static final double BIAS = 1e-8;

	public static void main(String[] args) {

		Raytracer R = new Raytracer(CAMERA_ORIGIN, CAMERA_PITCH, CAMERA_YAW, CAMERA_ROLL, XRES, YRES, FOV, AA, THREADS, TILE_SIZE, BIAS);

		R.addLight(new PointLight(new Vector(3d,3d,3d), 1450d, Color.WHITE));
		R.addLight(new PointLight(new Vector(-3d,3d,3d), 1450d, Color.WHITE));

		Material TMat = new Material(new DiffuseShader(Color.YELLOW,0.3d,R));
		AffineMatrix TMx = AffineMatrix.buildMatrix(0d, 0d, 0d, new Vector(0d,-1d,0d), 0.25d);
		Polyhedron TEAPOT = Polyhedron.loadOBJ(new File("dragon.obj"), TMat, TMx);
		TEAPOT.calculateTree();

		//Material PMat = new Material(new DiffuseShader(Color.CYAN, 0.18d, R));
		Material PMat = new Material(new ReflectionShader(Color.WHITE, 0.90d, R));
		AffineMatrix PMx = AffineMatrix.buildMatrix(0d, 0d, 0d, new Vector(0d,-1d,0d));
		Plane PLANE = new Plane(PMat, PMx);		

		R.addShape(TEAPOT);
		R.addShape(PLANE);

		R.write();

	}

}
