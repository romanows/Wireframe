
/**
 * A class that groups different "views" of the "same" point.
 * @author romanows
 */
public class PointGroup {
	/** A point defined with respect to the wireframe origin (the "abstract" or "ideal" points) */
	Point3d proto;
	
	/** Calculated, translated/rotated/scaled coords */
	Point3d world;
	Point2d proj;   // The value of this point projected on a 2d surface

	public PointGroup(Point3d p)   {
		this(p.x,p.y,p.z);
	}
	
	public PointGroup(double x, double y, double z)   {
		proto = new Point3d(x,y,z);
		world = new Point3d(x,y,z);
	}
}