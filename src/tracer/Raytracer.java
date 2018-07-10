package tracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import patchi.util.PatchiColor;
import tracer.shapes.Shape;

public class Raytracer {

	//Camera origin
	private Vector CO;
	private double[][] cspace;

	int xres;
	int yres;
	double xsize;
	double ysize;

	private double f;
	private double fov;

	private boolean AA;

	private ArrayList<Shape> shapes;

	public Raytracer(Vector CO, double pitch, double yaw, double roll, int xres, int yres, double f, double fovdeg, boolean AA) {

		shapes = new ArrayList<Shape>();

		this.CO = CO;
		this.f = f;

		this.AA = AA;

		double cx = Math.cos(Math.toRadians(pitch));
		double cy = Math.cos(Math.toRadians(yaw));
		double cz = Math.cos(Math.toRadians(roll));
		double sx = Math.sin(Math.toRadians(pitch));
		double sy = Math.sin(Math.toRadians(yaw));
		double sz = Math.sin(Math.toRadians(roll));

		cspace = new double[][]{
			{cy*cz,					-cy*sz,					sy,			CO.getX()},
			{(cz*sx*sy)+(cx*sz),	(cx*cz)-(sx*sy*sz),		-sx*cy,		CO.getY()},
			{(sx*sz)-(cx*cz*sy),	(cz*sx)+(cx*sy*sz),		cx*cy,		CO.getZ()},
			{0,						0,						0,			1}
		};

		//		System.out.println(cspace[0][0] + "," + cspace[0][1] + "," + cspace[0][2] + "," + cspace[0][3]);
		//		System.out.println(cspace[1][0] + "," + cspace[1][1] + "," + cspace[1][2] + "," + cspace[1][3]);
		//		System.out.println(cspace[2][0] + "," + cspace[2][1] + "," + cspace[2][2] + "," + cspace[2][3]);
		//		System.out.println(cspace[3][0] + "," + cspace[3][1] + "," + cspace[3][2] + "," + cspace[3][3]);

		this.xres = xres;
		this.yres = yres;
		fov = Math.toRadians(fovdeg);
		xsize = 2 * f * Math.tan(fov*0.5);
		ysize = (yres * xsize) / xres; 

	}

	public void write() {

		BufferedImage output = new BufferedImage(xres, yres, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < yres; y++) {

			int ytransform = yres - 1 - y;

			for(int x = 0; x < xres; x++) {

				Color C = renderPixel(x, ytransform);			
				output.setRGB(x, y, C.getRGB());

			}

		}

		try {
			ImageIO.write(output, "png", new File("output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addShape(Shape S) {
		shapes.add(S);
	}

	private Vector transformCameraSpace(Vector in) {

		double x = in.getX();
		double y = in.getY();
		double z = in.getZ();

		double xo = x*cspace[0][0] + y*cspace[0][1] + z*cspace[0][2] + cspace[0][3];
		double yo = x*cspace[1][0] + y*cspace[1][1] + z*cspace[1][2] + cspace[1][3];	
		double zo = x*cspace[2][0] + y*cspace[2][1] + z*cspace[2][2] + cspace[2][3];	

		return new Vector(xo,yo,zo);

	}

	private Ray cameraCast(int px, int py, double shiftx, double shifty) {

		double cx = (((double)px + shiftx)/ xres) * xsize - (xsize*0.5d);
		double cy = (((double)py + shifty)/ yres) * ysize - (ysize*0.5d);

		Vector dir = new Vector(CO, transformCameraSpace(new Vector(cx, cy, -f)));

		return new Ray(CO, dir);

	}

	private Color renderPixel(int x, int y) {

		Color C = Color.BLACK;

		if(!AA) {
			
			Ray R = cameraCast(x,y,0d,0d);
			C = shade(getIntersect(R));		
			
		} else {
			
			Ray R0 = cameraCast(x,y,0.5d,0.5d);
			Color C0 = shade(getIntersect(R0));
			Ray R1 = cameraCast(x,y,0.5d,-0.5d);
			Color C1 = shade(getIntersect(R1));	
			Ray R2 = cameraCast(x,y,-0.5d,0.5d);
			Color C2 = shade(getIntersect(R2));	
			Ray R3 = cameraCast(x,y,-0.5d,-0.50d);
			Color C3 = shade(getIntersect(R3));
			
			C = PatchiColor.average(C0,C1,C2,C3);
			
		}
		
		return C;

	}

	private Intersect getIntersect(Ray R) {

		Intersect V = null;

		for(Shape S : shapes) {

			Intersect n = S.intersect(R);

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

	private Color shade(Intersect I) {

		Color C = Color.BLACK;

		if(I == null) return C;

		Vector dIn = I.getInbound().getDirection();
		Vector dNorm = I.getNormal();

		double dot = dNorm.dot(dIn.negate());
		int facingShade = (int) (dot * 255d);

		C = new Color(facingShade,facingShade,facingShade);
		return C;

	}

}
