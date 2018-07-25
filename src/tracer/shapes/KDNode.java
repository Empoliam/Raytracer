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
	double mid;
	boolean leaf;

	KDNode leftnode;
	KDNode rightnode;


	public KDNode(Collection<Tri> tris) {

		box = new BoundingBox(tris);
		leaf = false;

		if(tris.size() > 20) {
									
			double xl = box.getXmax() - box.getXmin();
			double yl = box.getYmax() - box.getYmin();
			double zl = box.getZmax() - box.getZmin();

			if(xl > yl && xl > zl) {
				axis = 0;
				for(Tri T : tris) {
					mid += T.getMid().getX();
				};
				mid /= tris.size();
			} else if (yl > zl && yl > xl) {
				axis = 1;
				for(Tri T : tris) {
					mid += T.getMid().getY();
				};
				mid /= tris.size();
			} else {
				axis = 2;
				for(Tri T : tris) {
					mid += T.getMid().getZ();
				};
				mid /= tris.size();
			}

			LinkedList<Tri> left = new LinkedList<Tri>();
			LinkedList<Tri> right = new LinkedList<Tri>();

			for(Tri T : tris) {
				switch(axis) {

				case 0: if(T.getMid().getX() <= mid) { 
					left.add(T);
				} else {
					right.add(T); 
				}
				break;

				case 1: if(T.getMid().getY() <= mid) { 
					left.add(T);
				} else {
					right.add(T); 
				}
				break;

				case 2: if(T.getMid().getZ() <= mid) { 
					left.add(T);
				} else {
					right.add(T); 
				}
				break;

				}
			}
			
			leftnode = new KDNode(left);
			rightnode = new KDNode(right);

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
