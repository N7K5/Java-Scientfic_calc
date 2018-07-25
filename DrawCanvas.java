import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

class DrawCanvas extends JPanel {
	
	int block_size= 20;
	String eqn;
	int size;
	char mode;
	Expression ex= new Expression();
	
	void set_vars(String e, char m, int s) {
		eqn= e;
		size= s;
		mode= m;
		ex.setMode(m);
	}
	
    @Override
    public void paintComponent(Graphics g) {
       super.paintComponent(g);
       setBackground(new Color(220, 220, 220));
       
       g.setColor(new Color(200, 200, 200));
       
       for(int i=0; i<size; i+=block_size) {
    	   g.drawLine(0, i, size, i);
    	   g.drawLine(i, 0, i, size);
       }
       g.setColor(new Color(170, 170, 170));
       g.drawLine(0, size/2, size, size/2);
       g.drawLine(size/2, 0, size/2, size);
       
       double cur_x= 0.0;
       ex.set(eqn.replace("X", cur_x+""));
       double cur_y= Double.parseDouble( ex.evaluate());
       
       g.setColor(Color.RED);
       
       while(cur_x<size/block_size) {
    	   double next_x= cur_x+0.02;
    	   ex.set(eqn.replace("X", next_x+""));
    	   double next_y= Double.parseDouble( ex.evaluate());
    	   
    	   g.drawLine((int)(cur_x*(block_size/2))+size/2, (size/2)-(int)(cur_y*(block_size/2)), (int)(next_x*(block_size/2))+size/2, (size/2)-(int)(next_y*(block_size/2)));
    	   cur_x= next_x;
    	   cur_y= next_y;

       }
       cur_x=0.0;
       ex.set(eqn.replace("X", cur_x+""));
       cur_y= Double.parseDouble( ex.evaluate());
       while(cur_x>0-(size/block_size)) {
    	   double next_x= cur_x-0.02;
    	   ex.set(eqn.replace("X", next_x+""));
    	   double next_y= Double.parseDouble( ex.evaluate());
    	   
    	   g.drawLine((int)(cur_x*(block_size/2))+size/2, (size/2)-(int)(cur_y*(block_size/2)), (int)(next_x*(block_size/2))+size/2, (size/2)-(int)(next_y*(block_size/2)));
    	   cur_x= next_x;
    	   cur_y= next_y;
       }
       
       
       /*g.setColor(Color.YELLOW);
       g.drawLine(30, 40, 100, 200);
       g.drawOval(150, 180, 10, 10);
       g.drawRect(200, 210, 20, 30);
       g.setColor(Color.RED);
       g.fillOval(300, 310, 30, 50);
       g.fillRect(400, 350, 60, 50);

       g.setColor(Color.WHITE);
       g.setFont(new Font("Monospaced", Font.PLAIN, 12));
       g.drawString("Testing custom drawing ...", 10, 20);*/
    }
    
 }
