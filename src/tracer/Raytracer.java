package tracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import patchi.util.PatchiColor;
import tracer.light.Light;
import tracer.shader.FacingRatioShader;
import tracer.shapes.Shape;

public class Raytracer {

	//Camera origin
	private Vector CO;
	private AffineMatrix cspace;

	private final int xres;
	private final int yres;
	private final double xsize;
	private final double ysize;

	private final double f;
	private final double fov;

	private final boolean AA;
	private double BIAS = 1e-8;

	private final int THREADS;
	private final int TILESIZE;

	private ArrayList<Shape> shapes;
	private ArrayList<Light> lights;

	public Raytracer(Vector CO, double pitch, double yaw, double roll, int xres, int yres, double f, double fovdeg, boolean AA, int THREADS, int tilesize) {

		shapes = new ArrayList<Shape>();
		lights = new ArrayList<Light>();

		this.CO = CO;
		this.f = f;

		this.AA = AA;

		if(THREADS == -1) {
			this.THREADS = Runtime.getRuntime().availableProcessors();
		} else if(THREADS == 0) {
			this.THREADS = 1;
		} else {
			this.THREADS = THREADS;
		}

		this.TILESIZE = tilesize;

		cspace = AffineMatrix.buildMatrix(pitch, yaw, roll, CO);

		this.xres = xres;
		this.yres = yres;
		fov = Math.toRadians(fovdeg);
		xsize = 2 * f * Math.tan(fov*0.5);
		ysize = (yres * xsize) / xres; 

	}

	public void write() {

		BufferedImage output = new BufferedImage(xres, yres, BufferedImage.TYPE_INT_RGB);

		long start = System.nanoTime();

		int xtiles = xres / TILESIZE;
		int ytiles = yres / TILESIZE;

		if(xtiles % TILESIZE != 0) xtiles += 1;
		if(ytiles % TILESIZE != 0) ytiles += 1;

		ThreadPoolExecutor pool = new ThreadPoolExecutor(THREADS, THREADS, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

		for(int y = 0; y < ytiles; y ++) {

			for(int x = 0; x < xtiles; x ++) {				

				final int x0 = x;
				final int y0 = y;

				pool.execute(new Runnable() {

					@Override
					public void run() {				
						renderTile(output, x0 * TILESIZE, Math.min(xres, (x0+1)*TILESIZE), y0 * TILESIZE, Math.min(yres, (y0+1)*TILESIZE));			
					}
				});

			}

		}

		pool.shutdown();
		try {
			pool.awaitTermination(9, TimeUnit.HOURS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("Render in: " + ((System.nanoTime() - start)/1000000000d) + "s");

		try {
			ImageIO.write(output, "png", new File("output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addShape(Shape S) {
		shapes.add(S);
	}

	public void addLight(Light L) {
		lights.add(L);
	}

	private Ray cameraCast(int px, int py, double shiftx, double shifty) {

		double cx = (((double)px + shiftx)/ xres) * xsize - (xsize*0.5d);
		double cy = (((double)py + shifty)/ yres) * ysize - (ysize*0.5d);

		Vector dir = new Vector(CO, cspace.transform(new Vector(cx, cy, -f)));

		return new Ray(CO, dir);

	}

	private Color renderPixel(int x, int y) {

		int ytransform = yres - 1 - y;
		Color C = Color.BLACK;

		if(!AA) {

			Ray R = cameraCast(x,ytransform,0d,0d);
			C = getColor(getIntersect(R,true));		

		} else {

			Ray R0 = cameraCast(x,ytransform,0.5d,0.5d);
			Color C0 = getColor(getIntersect(R0,true));
			Ray R1 = cameraCast(x,ytransform,0.5d,-0.5d);
			Color C1 = getColor(getIntersect(R1,true));	
			Ray R2 = cameraCast(x,ytransform,-0.5d,0.5d);
			Color C2 = getColor(getIntersect(R2,true));	
			Ray R3 = cameraCast(x,ytransform,-0.5d,-0.50d);
			Color C3 = getColor(getIntersect(R3,true));

			C = PatchiColor.average(C0,C1,C2,C3);

		}

		return C;

	}

	private Intersect getIntersect(Ray R, boolean cullBackface) {

		Intersect V = null;

		for(Shape S : shapes) {

			Intersect n = S.intersect(R, cullBackface);

			if(n != null) {
				if(V != null) {
					if(V.getT() > n.getT()) {
						V = n;	
					}
				} else {
					V = n;
				}

			}

		}

		return V;

	}

	private Intersect getIntersect(Ray R, double maxDistance, boolean cullBackface) {

		Intersect I = getIntersect(R, cullBackface);
		if(I == null) return null;
		double t = I.getT();
		if(t*t > maxDistance) return null;
		return I;

	}

	private Color getColor(Intersect I) {

		if(I == null) return Color.BLACK;
		
		Color C = I.getShape().getColor();

		for(Light L : lights) {

			Vector shadowCorrection = I.getCoords().add(I.getNormal().scalarMult(BIAS));
			Vector lightDir = L.getDirection(shadowCorrection);

			Ray R = new Ray(shadowCorrection, lightDir);
			Intersect J = getIntersect(R, L.getDistanceSquare(I.getCoords()), false);

			if(J != null) C = Color.BLACK;

		}

		C = new FacingRatioShader().shade(I, C);

		return C;

	}

	private void renderTile(BufferedImage output, int xmin, int xmax, int ymin, int ymax) {

		for(int y = ymin; y < ymax; y++) {

			for(int x = xmin; x < xmax; x++) {

				Color C = renderPixel(x, y);			
				output.setRGB(x, y, C.getRGB());

			}

		}
	}

}
