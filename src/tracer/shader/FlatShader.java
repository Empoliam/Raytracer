package tracer.shader;

import java.awt.Color;

import tracer.Intersect;

public class FlatShader implements Shader {

	Color COLOR;
	
	public FlatShader(Color C) {
		this.COLOR = C;		
	}

	@Override
	public Color shade(Intersect I) {
		return COLOR;
	}

}
