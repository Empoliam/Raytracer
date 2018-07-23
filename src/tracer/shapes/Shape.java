package tracer.shapes;

import java.util.LinkedList;

import patchi.math.space.Ray;
import tracer.AffineMatrix;
import tracer.Intersect;
import tracer.shader.Material;
import tracer.shader.Shader;

public abstract class Shape {

	protected Material MATERIAL;
	protected AffineMatrix OBJECT_SPACE;
	
	protected Shape(Material mat, AffineMatrix M) {
		MATERIAL = mat;
		OBJECT_SPACE = M;
	}
	
	public abstract Intersect intersect(Ray R, boolean cullBackface);
		
	public LinkedList<Shader> getShaders() {
		return MATERIAL.getShaders();
	}
	
}
