package tracer;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import patchi.math.space.Vector;
import tracer.light.PointLight;
import tracer.shader.DiffuseShader;
import tracer.shader.Material;
import tracer.shader.ReflectionShader;
import tracer.shapes.Plane;
import tracer.shapes.Polyhedron;

public class Main {

	private static final Vector CAMERA_ORIGIN = new Vector(0d,1d,5d);
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

		R.addLight(new PointLight(new Vector(2d,2d,2d), 800d, Color.WHITE));
		R.addLight(new PointLight(new Vector(-2d,2d,2d), 800d, Color.WHITE));

		Material TMat = new Material(new DiffuseShader(Color.YELLOW,0.3d,R));
		AffineMatrix TMx = AffineMatrix.buildMatrix(0d, 0d, 0d, new Vector(1d,0d,-1d), 0.25d);
		Polyhedron TEAPOT = loadOBJ(new File("teapot.obj"), TMat, TMx);
		TEAPOT.calculateBounds();
		
		Material T2Mat = new Material(new DiffuseShader(Color.CYAN,0.3d,R));
		AffineMatrix T2Mx = AffineMatrix.buildMatrix(0d, 180d, 0d, new Vector(-1d,0d,-1d), 0.25d);
		Polyhedron TEAPOT2 = loadOBJ(new File("teapot.obj"), T2Mat, T2Mx);
		TEAPOT2.calculateBounds();
		
		
		Material PMat = new Material(new ReflectionShader(Color.WHITE, 0.9d, R));
		AffineMatrix PMx = AffineMatrix.buildMatrix(0d, 0d, 0d, new Vector(0d,0d,0d));
		Plane PLANE = new Plane(PMat, PMx);		
		
		R.addShape(TEAPOT);
		R.addShape(TEAPOT2);
		R.addShape(PLANE);

		R.write();

	}

	public static Polyhedron loadOBJ(File F, Material mat, AffineMatrix M) {

		try {
						
			BufferedReader br = new BufferedReader(new FileReader(F));

			LinkedList<Vector> vertexes = new LinkedList<>();
			Polyhedron P = new Polyhedron(mat, M);

			String l = br.readLine();
			while(l != null) {
								
				String[] line = l.split(" ");
				
				
				if(line[0].equals("v")) {
					
					vertexes.add(new Vector(Double.parseDouble(line[1]),Double.parseDouble(line[2]),Double.parseDouble(line[3])));
					
				} else if (line[0].equals("f")) {
												
					Vector[] verts = new Vector[line.length-1];
					
					for(int x = 1; x < line.length; x++) {
					
						int sp = Integer.parseInt(line[x].split("//")[0]);
						verts[x-1] = vertexes.get(sp-1);					
						
					}
				
					P.addFace(verts);
					
				} 
				
				l = br.readLine();
				
			}
			
			br.close();
			return P; 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		return null;

	}

}
