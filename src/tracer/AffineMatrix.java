package tracer;

import patchi.math.space.Vector;

public class AffineMatrix {

	private final double[][] MATRIX;
	private final Vector ORIGIN;

	public AffineMatrix(double[][] in) {

		MATRIX = in;
		this.ORIGIN = new Vector(in[0][3],in[1][3],in[2][3]);

	}

	public static AffineMatrix buildMatrix(double pitch, double yaw, double roll, Vector translation) {

		double cx = Math.cos(Math.toRadians(pitch));
		double cy = Math.cos(Math.toRadians(yaw));
		double cz = Math.cos(Math.toRadians(roll));
		double sx = Math.sin(Math.toRadians(pitch));
		double sy = Math.sin(Math.toRadians(yaw));
		double sz = Math.sin(Math.toRadians(roll));

		return new AffineMatrix(new double[][]{
			{cy*cz,					-cy*sz,					sy,			translation.getX()},
			{(cz*sx*sy)+(cx*sz),	(cx*cz)-(sx*sy*sz),		-sx*cy,		translation.getY()},
			{(sx*sz)-(cx*cz*sy),	(cz*sx)+(cx*sy*sz),		cx*cy,		translation.getZ()},
			{0,						0,						0,			1}
		});

	}
	
	public static AffineMatrix buildMatrix(double pitch, double yaw, double roll, Vector translation, double scale) {
		
		double[][] A = buildMatrix(pitch, yaw, roll, translation).getMatrix();
		
		A[0][0] *= scale;
		A[0][1] *= scale;
		A[0][2] *= scale;
		A[1][0] *= scale;
		A[1][1] *= scale;
		A[1][2] *= scale;
		A[2][0] *= scale;
		A[2][1] *= scale;
		A[2][2] *= scale;
		
		return new AffineMatrix(A);
		
	}

	public Vector transform(Vector V) {
		
		double x = V.getX();
		double y = V.getY();
		double z = V.getZ();

		double xo = x*MATRIX[0][0] + y*MATRIX[0][1] + z*MATRIX[0][2] + MATRIX[0][3];
		double yo = x*MATRIX[1][0] + y*MATRIX[1][1] + z*MATRIX[1][2] + MATRIX[1][3];	
		double zo = x*MATRIX[2][0] + y*MATRIX[2][1] + z*MATRIX[2][2] + MATRIX[2][3];
		
		return new Vector(xo,yo,zo);
		
	}

	public double[][] getMatrix() {
		return MATRIX;
	}

	public Vector getOrigin() {
		return ORIGIN;
	}
	
}
