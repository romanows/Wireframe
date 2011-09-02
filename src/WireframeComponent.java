import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;

import javax.swing.JComponent;


/**
 * Wraps a {@link Wireframe} for display.
 * See {@link Wireframe} for more details.
 * @author romanows
 */
public class WireframeComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	
	public final Wireframe wireframe;
	
	public WireframeComponent(Wireframe wireframe) {
		this.wireframe = wireframe;
	}
	

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g)   {
		int i;
		Graphics2D g2 = (Graphics2D)g;

		// FIXME: should we be able to change the screen origin?
		int cx = getSize().width / 2;
		int cy = getSize().height / 2;
		g2.translate(cx, cy);

		// FIXME: let's use some anonymous classes: g2.draw(new Line2D.Double(blah));  Cool?
		//	Unless this somehow makes drawing take longer, new versus modifying the shape.
		//	Wait till you can see the docs 
/*		
		Shape [] pts = new Line2D.Double[point.length]; //FIXME;			
		for(i=0; i<point.length; i++)   {		// Get projection of all points
			pts[i] = new Line2D.Double(point[i].proj.getX(), point[i].proj.getY(),
							point[i].proj.getX(), point[i].proj.getY()); //FIXME;			// Set points
			g2.draw(pts[i]);
		}
		*/
	
		g2.setStroke(new BasicStroke(4));
		g2.setPaint(Color.black);
		g2.setPaint(new GradientPaint(40,40,Color.blue,60,50,Color.white,true));
		
		Shape [] edges = new Line2D.Double[wireframe.edge.length];
		for(i=0; i<wireframe.edge.length; i++)   {
			edges[i] = new Line2D.Double(wireframe.edge[i].point1.proj.x,wireframe.edge[i].point1.proj.y,
					wireframe.edge[i].point2.proj.x,wireframe.edge[i].point2.proj.y);
			g2.draw(edges[i]);
		}
	}
}
