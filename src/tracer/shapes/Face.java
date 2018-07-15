package tracer.shapes;

import java.util.ArrayList;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.AffineMatrix;
import tracer.Intersect;
import tracer.shader.Material;

public class Face extends Shape {

	private ArrayList<Tri> TRI;
	
	public Face(Material mat, AffineMatrix M, Vector ... vertices) {
		
		super(mat, M);
		
		TRI = new ArrayList<Tri>();		
		for(int n = 2; n < vertices.length; n++) {
			TRI.add(new Tri(mat, M, 
					vertices[0], 
					vertices[n-1], 
					vertices[n]));	
			
		}
				
	}

	@Override
	public Intersect intersect(Ray R, boolean cullBackface) {

		Intersect I = null;
		
		for(Tri T : TRI) {
			I = T.intersect(R, cullBackface);
			if(I != null) break;
		}
		
		return I;
	}

	@Override
	public Vector getNormal(Vector V) {
		return TRI.get(0).getNormal(V);
	}

}
