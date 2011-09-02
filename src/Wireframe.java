

/**
 * Draws a 3d wireframe with some animation on a 2d surface.
 * 
 * <p>One of my first Java programs, worked out in my spare time, while in the Peace Corps, circa 2004.
 * Imagine coding by lantern light before school on laptop charge scrounged from a generator the night before. 
 * I worked the math out on paper but never transferred the documentation to the source code, sorry.
 * I've tidied/refactored the code somewhat, especially converting to javadoc and splitting the source into separate files.
 * However, most of the content (incorrect documentation especially) is otherwise original.
 * </p>
 * 
 * @author romanows
 */
public class Wireframe {
	
	// 3d Display/World/Screen constants...
	static final double screenDistance = 500.0; 
	static final double fieldOfView = 60.0 * Math.PI/180.0; 
	static final double viewWidth = 300.0; 
	static final double viewHeight = 300.0; 
	static final double screenArcLength = 2*Math.PI*screenDistance*fieldOfView/(2*Math.PI);

	PointGroup [] point;	// Array of points that define the wireframe
	Edge [] edge;			// Pairs of points defining the "wires" in the wireframe 
	PointGroup origin;		// Wireframe origin
	PointGroup moment;		// Wireframe point of rotation

	double theta = 0;		// Rotation around X axis
	double rho = 0;			// Rotation around Y axis
	double psi = 0;			// Rotation around Z axis
	double sx=1, sy=1, sz=1;	// Scaling along original axes

	
	public Wireframe(Point3d [] p, Point3d org, Point3d mom)  {
		int i;
		moment = new PointGroup(mom); origin = new PointGroup(org);
		point = new PointGroup[p.length];
		for(i=0; i<point.length; i++)   {
			point[i] = new PointGroup(p[i]);
		}
	}

	
	public Wireframe(double [][] p, double [] org, double [] mom)  {
		int i;
		moment = new PointGroup(mom[0],mom[1],mom[2]); origin = new PointGroup(org[0],org[1],org[2]);
		point = new PointGroup[p.length];
		for(i=0; i<point.length; i++)   {
			point[i] = new PointGroup(p[i][0],p[i][1],p[i][2]);
		}
	}
	

	/**
	 * Build a list of edges from pairs of indicies into the wireframes "points" array
	 * @param idx
	 */
	public void setEdges(int [][] idx)   {
		int i;

		edge = new Edge[idx.length];
		for(i=0; i<idx.length; i++)   {
			edge[i] = new Edge(point[idx[i][0]], point[idx[i][1]]);
		}
	}

 
	/**
	 * Rotates around an axis
	 * arguments are pt = {a,b,c}, moment = {j,k,l}, where abc=jkl, and for abc: 
	 * around the x-axis, a,b,c,ang = x,y,z,theta
	 * around the y-axis, a,b,c,ang = y,x,z,rho
	 * around the z-axis, a,b,c,ang = z,y,x,psi
	 * 
	 * @param pt
	 * @param moment
	 * @param ang
	 * @return
	 */
	private double [] rotateAxis(double [] pt, double [] moment, double ang) {
		double n,o;
		double s;
		double Q;
		double [] out = new double [3];

		n = pt[1] - moment[1];
		o = pt[2] - moment[2];

		s = Math.sqrt(n*n+o*o);

		Q = Math.atan2(n,o) + ang;

		out[0] = pt[0];
		out[1] = pt[1] + s*Math.sin(Q) - n;
		out[2] = pt[2] + s*Math.cos(Q) - o;

		return(out);
	}

	
	/**
	 * Rotates the point along the 3 axes angles, relative to a moment of rotation.
	 * 
	 * @param pt
	 * @param moment
	 * @return
	 */
	public Point3d rotate(Point3d pt, Point3d moment)   {
		double [] out,out2;
		
		// Rotate along one axis, then the next, then the next :(
		out = rotateAxis(new double [] {pt.x,pt.y,pt.z}, new double [] {moment.x,moment.y,moment.z}, theta);
		out2 = rotateAxis(new double [] {out[1],out[0],out[2]}, new double [] {moment.x,moment.y,moment.z}, rho);
		out[0] = out2[1];
		out[1] = out2[0];
		out[2] = out2[2];
		out2 = rotateAxis(new double [] {out[2],out[1],out[0]}, new double [] {moment.x,moment.y,moment.z}, psi);
		out[0] = out2[2];
		out[1] = out2[1];
		out[2] = out2[0];

		return(new Point3d(out[0], out[1], out[2]));
	}

	
	/**
	 * Scales the wireframe by a factor
	 * @param s
	 */
	public void scale(double s)   {
		sx *= s; sy *= s; sz *= s;
	}
	

	/**
	 * Returns the 2d point resulting from the 3d point's projection on the screen.
	 * @param pt
	 * @return
	 */
	private Point2d projectPoint(Point3d pt)   {
		double tx,ty;

		tx = pt.x * screenDistance / pt.z;
		ty = pt.y * screenDistance / pt.z;

		return( new Point2d(tx,ty) );
	}
	

	/** Transforms the prototype shape given the rotation, scaling, and translation values */
	public void update()   {
		int i;

		for(i=0; i<point.length; i++)   {
			// translate prototype origin, scale
			// FIXME: is the prototype origin of any use?  Maybe for a complex shape when 
			// we decide that the origin should move...  maybe works for, say, choosing the 
			// point of reference to be a corner instead of the center?
			point[i].world.x = (sx * (point[i].proto.x-origin.proto.x));
			point[i].world.y = (sy * (point[i].proto.y-origin.proto.y));
			point[i].world.z = (sz * (point[i].proto.z-origin.proto.z));

			// rotate points around moment
//			point[i].world = rotate(point[i].world, moment.proto);
			point[i].world = rotate(point[i].world, new Point3d(0,0,0));

			// translate shape 
			point[i].world.x = point[i].world.x + origin.world.x;
			point[i].world.y = point[i].world.y + origin.world.y;
			point[i].world.z = point[i].world.z + origin.world.z;

			// Project the shape 
			point[i].proj = projectPoint(point[i].world);
		}
	}	
}
