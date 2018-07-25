import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;

public class Num_Convertor extends JFrame {

	private JPanel contentPane;
	private JTextField var_tf_dec;
	private JTextField var_tf_bin;
	private JTextField var_tf_hex;
	private JTextField var_tf_oct;
	NumBase num;
	Num_Convertor this_obj;

	/**
	 * Create the frame.
	 */
	public Num_Convertor() {
		this_obj= this;
		num= new NumBase();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		var_tf_dec = new JTextField();
		var_tf_dec.setBackground(new Color(211, 211, 211));
		var_tf_dec.setHorizontalAlignment(SwingConstants.CENTER);
		var_tf_dec.setFont(new Font("Dialog", Font.BOLD, 12));
		var_tf_dec.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				clear_display();
			}
		});
		var_tf_dec.setBounds(96, 53, 418, 32);
		contentPane.add(var_tf_dec);
		var_tf_dec.setColumns(10);
		
		var_tf_bin = new JTextField();
		var_tf_bin.setBackground(new Color(211, 211, 211));
		var_tf_bin.setHorizontalAlignment(SwingConstants.CENTER);
		var_tf_bin.setFont(new Font("Dialog", Font.BOLD, 12));
		var_tf_bin.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				clear_display();
			}
		});
		var_tf_bin.setColumns(10);
		var_tf_bin.setBounds(96, 129, 418, 32);
		contentPane.add(var_tf_bin);
		
		var_tf_hex = new JTextField();
		var_tf_hex.setBackground(new Color(211, 211, 211));
		var_tf_hex.setHorizontalAlignment(SwingConstants.CENTER);
		var_tf_hex.setFont(new Font("Dialog", Font.BOLD, 12));
		var_tf_hex.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				clear_display();
			}
		});
		var_tf_hex.setColumns(10);
		var_tf_hex.setBounds(96, 205, 418, 32);
		contentPane.add(var_tf_hex);
		
		var_tf_oct = new JTextField();
		var_tf_oct.setBackground(new Color(211, 211, 211));
		var_tf_oct.setHorizontalAlignment(SwingConstants.CENTER);
		var_tf_oct.setFont(new Font("Dialog", Font.BOLD, 12));
		var_tf_oct.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				clear_display();
			}
		});
		var_tf_oct.setColumns(10);
		var_tf_oct.setBounds(96, 281, 418, 32);
		contentPane.add(var_tf_oct);
		
		JButton btnConvart = new JButton("Convart");
		btnConvart.setBackground(new Color(102, 205, 170));
		btnConvart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tf_dec= var_tf_dec.getText();
				String tf_oct= var_tf_oct.getText();
				String tf_bin= var_tf_bin.getText();
				String tf_hex= var_tf_hex.getText();
				if(tf_dec.length()>0) {
					num.set_dec(tf_dec);
					update_display();
				}
				else if(tf_bin.length()>0) {
					num.set_bin(tf_bin);
					update_display();
				}
				else if(tf_hex.length()>0) {
					num.set_hex(tf_hex);
					update_display();
				}
				else if(tf_oct.length()>0) {
					num.set_oct(tf_oct);
					update_display();
				}
				else {
					clear_display();
				}
					
			}
		});
		btnConvart.setBounds(397, 369, 117, 25);
		contentPane.add(btnConvart);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBackground(new Color(255, 182, 193));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				this_obj.dispose();
			}
		});
		btnClose.setBounds(268, 369, 117, 25);
		contentPane.add(btnClose);
		
		JLabel lblDec = new JLabel("Dec");
		lblDec.setBounds(30, 61, 70, 15);
		contentPane.add(lblDec);
		
		JLabel lblBin = new JLabel("Bin");
		lblBin.setBounds(30, 137, 70, 15);
		contentPane.add(lblBin);
		
		JLabel lblHex = new JLabel("Hex");
		lblHex.setBounds(30, 213, 70, 15);
		contentPane.add(lblHex);
		
		JLabel lblOct = new JLabel("Oct");
		lblOct.setBounds(30, 289, 70, 15);
		contentPane.add(lblOct);
	}
	
	void update_display() {
		clear_display();
		var_tf_dec.setText(num.get_dec());
		var_tf_oct.setText(num.get_oct());
		var_tf_bin.setText(num.get_bin());
		var_tf_hex.setText(num.get_hex());
	}
	void clear_display() {
		var_tf_dec.setText("");
		var_tf_oct.setText("");
		var_tf_bin.setText("");
		var_tf_hex.setText("");
	}
}
