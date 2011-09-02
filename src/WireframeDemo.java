import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TimerTask;

import javax.swing.JFrame;


/**
 * Draws and animates a wireframe object.  
 * See {@link Wireframe} for more details.
 * @author romanows
 */
public class WireframeDemo {

	private static Wireframe getWireframe() {
		Wireframe cube = new Wireframe( new double [][] { 
				{1,1,31},
				{-1,1,31},
				{-1,1,29},
				{1,1,29},
				{1,-1,31},
				{-1,-1,31},
				{-1,-1,29},
				{1,-1,29},

				{4,4,31},
				{2,4,31},
				{2,4,29},
				{4,4,29},
				{4,2,31},
				{2,2,31},
				{2,2,29},
				{4,2,29}}, new double [] {0,0,30}, new double [] {0,0,30});
		
		/*
		Wireframe cube = new Wireframe( new double [][] { 
						{3,3,3},
						{0,3,3},
						{0,3,0},
						{3,3,0},
						{3,0,3},
						{0,0,3},
						{0,0,0},
						{3,0,0}}, new double [] {0,0,0}, new double [] {0,0,0});
						*/
		
		cube.setEdges(new int [][] {{0,1},{1,2},{2,3},{3,0},
				{4,5},{5,6},{6,7},{7,4},
				{0,4},{1,5},{2,6},{3,7},
		
				{8,9},{9,10},{10,11},{11,8},
				{12,13},{13,14},{14,15},{15,12},
				{8,12},{9,13},{10,14},{11,15}});
		
		cube.scale(3);
		cube.update();
		return cube;
	}

	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Wireframe");
		Container c = frame.getContentPane();
		final Wireframe wireframe = getWireframe(); 
		final WireframeComponent cubeComponent = new WireframeComponent(wireframe);
		c.add(cubeComponent);

		// Animate the cube
		java.util.Timer timer = new java.util.Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				wireframe.theta = (wireframe.theta + (2*Math.PI/120)) % (2*Math.PI);
				wireframe.rho = (wireframe.rho + (2*Math.PI/120)) % (2*Math.PI);
				wireframe.psi = (wireframe.psi + (2*Math.PI/120)) % (2*Math.PI);
				wireframe.update();
				cubeComponent.repaint();
			}
		};
		timer.schedule(task, 0, 50);
	
		cubeComponent.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me)   {
				wireframe.rho = (wireframe.rho + (2*Math.PI/12)) % (2*Math.PI);
				wireframe.update();
				cubeComponent.repaint();
			}
		});
		
		frame.setSize(800,800);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setVisible(true);
	}
}
