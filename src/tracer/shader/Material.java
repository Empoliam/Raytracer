package tracer.shader;

import java.util.LinkedList;

public class Material {

	private LinkedList<Shader> SHADERS;
	
	public Material(Shader ... shaders) {
		
		SHADERS = new LinkedList<Shader>();
		for(int s = 0; s < shaders.length; s++) {
			SHADERS.add(shaders[s]);
		}
		
	}
	
	public void addShader(Shader S) {
		SHADERS.add(S);
	}
	
	public LinkedList<Shader> getShaders(){
		return SHADERS;
	}

}
