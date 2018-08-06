package tracer.shapes;

import java.util.Collection;
import java.util.LinkedList;

import patchi.math.space.Ray;
import tracer.BoundingBox;
import tracer.Intersect;

public class KDNode {

	BoundingBox box;
	Collection<Tri> TRIS;

	/* 0 x
	 * 1 y
	 * 2 z
	 */
	int axis;
	boolean leaf;

	KDNode leftnode;
	KDNode rightnode;


	public KDNode(Collection<Tri> tris, int depth) {

		box = new BoundingBox(tris);
		leaf = false;

		//		System.out.println(box);
		//		System.out.println("axis: " + axis);
		//		System.out.println("tris: " + tris.size());	

		if(depth < 20 && tris.size() > 5) {

			double l[] = new double[3];

			l[0] = box.getXmax() - box.getXmin();
			l[1] = box.getYmax() - box.getYmin();
			l[2] = box.getZmax() - box.getZmin();

			double splitpoint = 0d;

			if(l[0] > l[1] && l[0] > l[2]) {
				axis = 0;
			} else if (l[1] > l[2] && l[1] > l[0]) {
				axis = 1;
			} else {
				axis = 2;
			}

			double cost = Double.POSITIVE_INFINITY;

			for(double s = 0.2d; s <= 0.8d; s += 0.2d) {

				double splittest = box.getImin(axis) + (s * l[1]);

				int ls = 0;
				int rs = 0;

				for(Tri T : tris) {
					if(T.getMid().getI(axis) < splittest) {
						ls++;
					} else {
						rs++;
					};
				};

				double ncost = 1d + (s * ls * 10d) + ((1d - s) * rs * 10d);

				if(ncost < cost) {
					cost = ncost;
					splitpoint = splittest;
				}

			} 

			//System.out.println("split at: " + splitpoint);

			LinkedList<Tri> left = new LinkedList<Tri>();
			LinkedList<Tri> right = new LinkedList<Tri>();

			for(Tri T : tris) {

				//System.out.println(T.getMid().getI(axis));

				if(T.getMid().getI(axis) <= splitpoint) { 
					left.add(T);
				} else {
					right.add(T); 
				}

			}

			leftnode = new KDNode(left, depth+1);
			rightnode = new KDNode(right, depth+1);

		} else {
			leaf = true;
			TRIS = tris;
		}

	}

	public Intersect intersect(Ray R, boolean cullBackface) {

		Intersect I = null;

		if(box.intersect(R)) {

			if(!leaf) {

				Intersect LI = leftnode.intersect(R, cullBackface);
				Intersect RI = rightnode.intersect(R, cullBackface);

				if(LI == null && RI == null) {
					I = null;
				} else if (LI == null) {
					I = RI;
				} else if (RI == null) {
					I = LI;
				} else {
					if(LI.getT() <= RI.getT()) {
						I = LI;
					} else {
						I = RI;
					}
				}

			} else {

				for(Tri T : TRIS) {

					Intersect P = T.intersect(R, cullBackface);

					if(P != null) {
						if(I == null) {
							I = P;
						} else if (I.getT() > P.getT()) {
							I = P;
						}

					}

				}

			}

		}

		return I;

	}

}
