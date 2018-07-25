package tracer.shapes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import patchi.math.space.Ray;
import patchi.math.space.Vector;
import tracer.AffineMatrix;
import tracer.Intersect;
import tracer.shader.Material;

public class Polyhedron extends Shape {

	private ArrayList<Tri> TRIS;
	
	private KDNode KDTREE;
	
	public Polyhedron(Material mat, AffineMatrix M) {

		super(mat, M);
		TRIS = new ArrayList<Tri>();		

	}

	@Override
	public Intersect intersect(Ray R, boolean cullBackface) {
		
		Intersect I = KDTREE.intersect(R, cullBackface);
		
		return I;

	}

	public void addFace(Vector ... vertices) {

		ArrayList<Tri> face = new Face(super.MATERIAL, super.OBJECT_SPACE, vertices).getTris();
		for(Tri T : face) {
			TRIS.add(T);
		}

	}

	public ArrayList<Tri> getTris() {
		return TRIS;
	}
	
	public void calculateTree() {
		KDTREE = new KDNode(TRIS);
		System.out.println("Tree calculated");
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
	
	public static Polyhedron loadOBJ(File F, Material mat, AffineMatrix M) {

		try {
						
			BufferedReader br = new BufferedReader(new FileReader(F));

			LinkedList<Vector> vertexes = new LinkedList<>();
			Polyhedron P = new Polyhedron(mat, M);

			String l = br.readLine();
			while(l != null) {
								
				String[] line = l.split(" ");
				
				
				if(line[0].equals("v")) {
					
					vertexes.add(new Vector(Double.parseDouble(line[1]),Double.parseDouble(line[2]),Double.parseDouble(line[3])));
					
				} else if (line[0].equals("f")) {
												
					Vector[] verts = new Vector[line.length-1];
					
					for(int x = 1; x < line.length; x++) {
					
						int sp = Integer.parseInt(line[x].split("//")[0]);
						verts[x-1] = vertexes.get(sp-1);					
						
					}
				
					P.addFace(verts);
					
				} 
				
				l = br.readLine();
				
			}
			
			br.close();
			return P; 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		return null;

	}

}
