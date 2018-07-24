package tracer;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.shapes.Face;
import tracer.shapes.Polyhedron;
import tracer.shapes.Tri;

public class BoundingBox {

	private double xmax = 0d;
	private double xmin = 0d;
	private double ymax = 0d;
	private double ymin = 0d;
	private double zmax = 0d;
	private double zmin = 0d;
	
	public BoundingBox(Polyhedron P) {
		
		for (Face F : P.getFaces()) {
			
			for(Tri T : F.getTris()) {
				
				double x = Math.min(T.getA().getX(), Math.min(T.getB().getX(), T.getC().getX()));
				double y = Math.min(T.getA().getY(), Math.min(T.getB().getY(), T.getC().getY()));
				double z = Math.min(T.getA().getZ(), Math.min(T.getB().getZ(), T.getC().getZ()));
				
				if(x > xmax) xmax = x;
				if(x < xmin) xmin = x;

				if(y > ymax) ymax = y;
				if(y < ymin) ymin = y;

				if(z > zmax) zmax = z;
				if(z < zmin) zmin = z;
				
			}
			
		}
		
	}
	
	public boolean intersect(Ray R) {
		
		Vector O = R.getOrigin();
		Vector D = R.getDirection();
		
		double tmin = (xmin - O.getX()) / D.getX();
		double tmax = (xmax - O.getX()) / D.getX();
		
		if(tmin > tmax) {
			double tmp = tmin;
			tmin = tmax;
			tmax = tmp;
		}
		
		double tymin = (ymin - O.getY()) / D.getY();
		double tymax = (ymax - O.getY()) / D.getY();
		
		if(tymin > tymax) {
			double tmp = tymin;
			tymin = tymax;
			tymax = tmp;
		}
		
		if(tymax < tmin || tmax < tymin) return false;
		
		if(tmin < tymin) tmin = tymin;
		
		double tzmin = (zmin - O.getZ()) / D.getZ();
		double tzmax = (ymax - O.getZ()) / D.getZ();
		
		if(tzmin > tzmax) {
			double tmp = tzmin;
			tzmin = tzmax;
			tzmax = tmp;
		}
		
		if(tzmax < tmin || tmax < tzmin) return false;		
		
		return true;
		
	}
	
	public double getXmax() {
		return xmax;
	}

	public double getXmin() {
		return xmin;
	}

	public double getYmax() {
		return ymax;
	}

	public double getYmin() {
		return ymin;
	}

	public double getZmax() {
		return zmax;
	}

	public double getZmin() {
		return zmin;
	}
	
}
