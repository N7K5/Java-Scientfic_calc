import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class CGTemplate extends JFrame {
   public static final int CANVAS_SIZE  = 600;
 
   private DrawCanvas canvas= new DrawCanvas();
 
   public CGTemplate() {
      
   }
   
   public void show_() {
	   
	   canvas.setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
	 
	   Container cp = getContentPane();
	   cp.add(canvas);
	      
	   //canvas.repaint();

	   setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	   pack();
	   setTitle("Graph Ploter...");
	   setResizable(false);
	   setVisible(true);
   }
   
   void plot(String eqn, char mode) {
	   canvas.set_vars(eqn, mode, CANVAS_SIZE);
	   setTitle(eqn);
   }
}

