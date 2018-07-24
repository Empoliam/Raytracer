package tracer.shapes;

import java.util.ArrayList;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.AffineMatrix;
import tracer.BoundingBox;
import tracer.Intersect;
import tracer.shader.Material;

public class Polyhedron extends Shape {

	private ArrayList<Face> FACE;
	private BoundingBox BOUNDS;

	public Polyhedron(Material mat, AffineMatrix M) {

		super(mat, M);
		FACE = new ArrayList<Face>();		
		calculateBounds();

	}

	@Override
	public Intersect intersect(Ray R, boolean cullBackface) {

		Intersect I = null;

		if(BOUNDS.intersect(R)) {
			
			for(Face F : FACE) {
				Intersect P = F.intersect(R, cullBackface);

				if(P != null) {
					if(I == null) {
						I = P;
					} else if (I.getT() > P.getT()) {
						I = P;
					}
				}

			}
			
		}

		return I;

	}

	public void addFace(Vector ... vertices) {

		FACE.add(new Face(super.MATERIAL, super.OBJECT_SPACE, vertices));

	}

	public static Polyhedron buildCube(Material mat, AffineMatrix M, double sideLength) {


		double dF = sideLength * 0.5d;
		Polyhedron P = new Polyhedron(mat, M);

		P.addFace(new Vector(dF,dF,dF), new Vector(-dF,dF,dF), new Vector(-dF,-dF,dF), new Vector(dF,-dF,dF));
		P.addFace(new Vector(dF,dF,-dF), new Vector(dF,-dF,-dF), new Vector(-dF,-dF,-dF), new Vector(-dF,dF,-dF));
		P.addFace(new Vector(dF,dF,dF), new Vector(dF,-dF,dF), new Vector(dF,-dF,-dF), new Vector(dF,dF,-dF));
		P.addFace(new Vector(dF,dF,dF), new Vector(dF,dF,-dF), new Vector(-dF,dF,-dF), new Vector(-dF,dF,dF));
		P.addFace(new Vector(-dF,dF,dF), new Vector(-dF,dF,-dF), new Vector(-dF,-dF,-dF), new Vector(-dF,-dF,dF));
		P.addFace(new Vector(dF,-dF,dF), new Vector(-dF,-dF,dF), new Vector(-dF,-dF,-dF), new Vector(dF,-dF,-dF));

		return P;
	}

	public ArrayList<Face> getFaces() {
		return FACE;
	}

	public void calculateBounds() {

		BOUNDS = new BoundingBox(this);

	}

}
