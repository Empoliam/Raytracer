package tracer.shapes;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.AffineMatrix;
import tracer.Intersect;
import tracer.shader.Material;

public class Plane extends Shape {

	private final Vector normal;

	public Plane(Material mat, AffineMatrix M) {

		super(mat, M);
		normal = new Vector(M.getOrigin(),M.transform(new Vector(0d,1d,0d)));

	}

	@Override
	public Intersect intersect(Ray R, boolean cullBackface) {

		Vector dN = R.getDirection().negate();
		Vector O = R.getOrigin();	

		Vector AO = new Vector(super.OBJECT_SPACE.getOrigin(),O);

		double modM = dN.dot(normal);
		if(modM < 0 && cullBackface) return null;

		double t = AO.dot(normal) / modM;
		if(t < 0) {
			return null;
		}

		return new Intersect(t, R.getPointFromParameter(t), this, R, normal);

	}

}
