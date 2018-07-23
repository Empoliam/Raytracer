package tracer.shader;

import java.awt.Color;

import patchi.math.PatchiMath;
import patchi.math.space.Ray;
import patchi.math.space.Vector;
import patchi.util.PatchiColor;
import tracer.Intersect;
import tracer.Raytracer;

public class ReflectionShader implements Shader {

	private final Raytracer RAYTRACER;

	private final Color COLOR;
	private final float REFLECTIVITY;
	private final double BIAS;

	public ReflectionShader(Color color, double reflectivity, Raytracer R) {

		this.COLOR = color;
		this.REFLECTIVITY = (float) PatchiMath.cutDoubleToRange(reflectivity, 0d, 1d);
		this.BIAS = R.getBias();
		this.RAYTRACER = R;

	}

	@Override
	public Color shade(Intersect I) {

		Color C = RAYTRACER.getBGColor(); 

		Vector N = I.getNormal();
		Vector D = I.getInbound().getDirection();
		Vector O = I.getCoords().add(N.scalarMult(BIAS));

		Vector R = D.add(N.scalarMult(N.dot(D)*2d).negate());

		Ray refRay = new Ray(O, R);

		Intersect reflection = RAYTRACER.getIntersect(refRay, true);

		C = PatchiColor.multiply(RAYTRACER.getColor(reflection),COLOR);

		if(REFLECTIVITY != 1f) {
			Color Dif = new DiffuseShader(COLOR, 1d - (double)REFLECTIVITY, RAYTRACER).shade(I);
			C = PatchiColor.scalarMultiply(C, REFLECTIVITY);
			C = PatchiColor.linearDodge(C,Dif);	
		}
		return C;

	}

}
