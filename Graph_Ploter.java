import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Graph_Ploter extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Graph_Ploter frame = new Graph_Ploter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Graph_Ploter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(29, 65, 388, 42);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnPlot = new JButton("Plot");
		btnPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				SwingUtilities.invokeLater(new Runnable() {
			         @Override
			         public void run() {
			        	try {
			        		CGTemplate graphPaper= new CGTemplate();
			        		graphPaper.plot(textField.getText(), 'd');
			            	graphPaper.show_();
			        	}
			        	 catch(Exception we) {
			        		 
			        	 }
			         }
			      });
				
				
			}
		});
		btnPlot.setBounds(300, 188, 117, 25);
		contentPane.add(btnPlot);
	}
}
