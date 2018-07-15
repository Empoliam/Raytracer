package tracer.shapes;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.AffineMatrix;
import tracer.Intersect;
import tracer.shader.Material;

public class Tri extends Shape {

	private final Vector A;
	private final Vector AB;
	private final Vector AC;

	private Vector normal;

	public Tri(Material mat, AffineMatrix M, Vector iA, Vector iB, Vector iC) {

		super(mat, M);
		
		Vector A = M.transform(iA);
		Vector B = M.transform(iB);
		Vector C = M.transform(iC);
		
		this.A = A;
		AB = new Vector(A, B);
		AC = new Vector(A, C);

		normal = AB.cross(AC).normalize();

	}

	public void flipNormal() {
		this.normal = this.normal.negate();
	}

	@Override
	public Intersect intersect(Ray R, boolean cullBackface) {

		Vector dN = R.getDirection().negate();
		Vector O = R.getOrigin();	

		Vector AO = new Vector(A,O);

		double modM = dN.dot(AB.cross(AC));
		if(modM < 0 && cullBackface) return null;

		double t = AO.dot(AB.cross(AC)) / modM;
		if(t < 0) return null;

		double u = dN.dot(AO.cross(AC)) / modM;
		if(u < 0 || u > 1) return null;

		double v = dN.dot(AB.cross(AO)) / modM;
		if(v < 0 || u > 1 || u+v > 1) return null;

		return new Intersect(t, R.getPointFromParameter(t), this, R);

	}

	@Override
	public Vector getNormal(Vector V) {
		return normal;
	}

}
