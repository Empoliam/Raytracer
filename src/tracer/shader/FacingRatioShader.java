package tracer.shader;

import java.awt.Color;

import patchi.math.space.Vector;
import patchi.util.PatchiColor;
import tracer.Intersect;

public class FacingRatioShader implements Shader {

	public FacingRatioShader() {}

	@Override
	public Color shade(Intersect I, Color C) {
		
		Vector dIn = I.getInbound().getDirection();
		Vector dNorm = I.getNormal();

		double dot = dNorm.dot(dIn.negate());

		C = PatchiColor.scalarMultiply(C, (float) dot);
		
		return C;
		
	}

}
