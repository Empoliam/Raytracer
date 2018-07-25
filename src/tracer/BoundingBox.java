package tracer;

import java.util.Collection;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.shapes.Tri;

public class BoundingBox {

	private double[] x;
	private double[] y;
	private double[] z;
	
	public BoundingBox(Collection<Tri> tris) {

		boolean init = false;
		x = new double[2];
		y = new double[2];
		z = new double[2];
		
		for(Tri T : tris) {

			double txm = Math.min(T.getA().getX(), Math.min(T.getB().getX(), T.getC().getX()));
			double tym = Math.min(T.getA().getY(), Math.min(T.getB().getY(), T.getC().getY()));
			double tzm = Math.min(T.getA().getZ(), Math.min(T.getB().getZ(), T.getC().getZ()));
			double txx = Math.max(T.getA().getX(), Math.max(T.getB().getX(), T.getC().getX()));
			double tyx = Math.max(T.getA().getY(), Math.max(T.getB().getY(), T.getC().getY()));
			double tzx = Math.max(T.getA().getZ(), Math.max(T.getB().getZ(), T.getC().getZ()));
			
			if(init) {

				x[1] = txx;
				x[0] = txm;
				y[1] = tyx;
				y[0] = tym;
				z[1] = tzx;
				z[0] = tzm;
				
				init = true;
				
			} else {

				if(txx > x[1]) x[1] = txx;
				if(txm < x[0]) x[0] = txm;

				if(tyx > y[1]) y[1] = tyx;
				if(tym < y[0]) y[0] = tym;

				if(tzx > z[1]) z[1] = tzx;
				if(tzm < z[0]) z[0] = tzm;

			}
		}
		
	}

	public boolean intersect(Ray R) {

		Vector O = R.getOrigin();
		Vector D = R.getDirection();

		int[] sign = new int[3];
		sign[0] = ((1d / D.getX()) < 0d) ? 1 : 0;
		sign[1] = ((1d / D.getY()) < 0d) ? 1 : 0;
		sign[2] = ((1d / D.getZ()) < 0d) ? 1 : 0;

		double tmin = (x[sign[0]] - O.getX()) / D.getX();
		double tmax = (x[1 - sign[0]] - O.getX()) / D.getX();

		double tymin = (y[sign[1]] - O.getY()) / D.getY();
		double tymax = (y[1 - sign[1]] - O.getY()) / D.getY();

		if(tmin > tymax || tymin > tmax) return false;

		if(tymin > tmin) tmin = tymin;
		if(tymax < tmax) tmax = tymax;

		double tzmin = (z[sign[2]] - O.getZ()) / D.getZ();
		double tzmax = (z[1 - sign[2]] - O.getZ()) / D.getZ();

		if(tmin > tzmax || tzmin > tmax) return false;	
		
		if(tzmin > tmin) tmin = tzmin;
		if(tzmax < tmax) tmax = tzmax;

		if(tzmin < 0 && tzmax < 0) return false;
		
		return true;

	}

	@Override

	public String toString() {
		return(x[0] + "," + x[1] + "   " + y[0] + "," + y[1] + "   " + z[0] + "," + z[1]);
	}

	public double getXmax() {
		return x[1];
	}

	public double getXmin() {
		return x[0];
	}

	public double getYmax() {
		return y[1];
	}

	public double getYmin() {
		return y[0];
	}

	public double getZmax() {
		return z[1];
	}

	public double getZmin() {
		return z[0];
	}

}
