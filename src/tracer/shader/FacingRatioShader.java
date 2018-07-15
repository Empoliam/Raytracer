package tracer.shader;

import java.awt.Color;

import patchi.math.space.Vector;
import patchi.util.PatchiColor;
import tracer.Intersect;

public class FacingRatioShader implements Shader {

	Color COLOR;
	
	public FacingRatioShader(Color C) {
		COLOR = C;		
	}

	@Override
	public Color shade(Intersect I) {
		
		Color C = null;
		
		Vector dIn = I.getInbound().getDirection();
		Vector dNorm = I.getNormal();

		double dot = dNorm.dot(dIn.negate());

		C = PatchiColor.scalarMultiply(COLOR, (float) dot);
		
		return C;
		
	}

}
