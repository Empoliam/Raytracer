package tracer.shader;

import java.awt.Color;
import java.util.ArrayList;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import patchi.util.PatchiColor;
import tracer.Intersect;
import tracer.Raytracer;
import tracer.light.Light;

public class DiffuseShader implements Shader {

	private ArrayList<Light> LIGHTS;
	
	private final Raytracer RAYTRACER;
	private final double BIAS;
	
	private final Color COLOR;
	private final double ALBEDO;
	
	public DiffuseShader(Color color, double albedo, Raytracer R) {

		RAYTRACER = R;
		
		LIGHTS = R.getLights();
		BIAS = R.getBias();
		
		COLOR = color;
		ALBEDO = albedo / Math.PI;
	}

	@Override
	public Color shade(Intersect I) {

		Vector surfaceN = I.getNormal();
		Vector P = I.getCoords();
		ArrayList<Color> colors = new ArrayList<Color>();
		
		for(Light L : LIGHTS) {
			
			Vector shadowCorrection = P.add(surfaceN.scalarMult(BIAS));
			Vector lightDir = L.getDirection(shadowCorrection);

			Ray R = new Ray(shadowCorrection, lightDir);
			Intersect J = RAYTRACER.getIntersect(R, L.getDistanceSquare(P), false);

			double dot = surfaceN.dot(R.getDirection());
			
			if(J != null) {
				colors.add(Color.BLACK);
			} else {	
				double m = L.getIntensity(P) * ALBEDO * dot;
				Color C = PatchiColor.scalarMultiply(COLOR, (float) m);
				colors.add(PatchiColor.multiply(C, L.getColor()));	
			}		
		}
		
		return PatchiColor.linearDodge(colors.toArray(new Color[0]));
		
	}
	
}
