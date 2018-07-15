package tracer.shader;

import java.awt.Color;

import tracer.Intersect;

public interface Shader {
	
	public Color shade(Intersect I);
		
}
