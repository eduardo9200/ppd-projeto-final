package telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Info extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JLabel lblInfo;
	private JLabel lblIdSpace;
	private JButton btnOk;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Info frame = new Info("");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public Info(String message) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 170);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblInfo = new JLabel(message);
		lblInfo.setBounds(5, 5, 324, 36);
		contentPane.add(lblInfo);
		
		lblIdSpace = new JLabel("");
		lblIdSpace.setBounds(5, 52, 319, 28);
		contentPane.add(lblIdSpace);
		
		btnOk = new JButton("OK");
		btnOk.setBounds(112, 97, 89, 23);
		contentPane.add(btnOk);
		
		initActions();
	}
	
	private void initActions() {
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	public void setLblInfo(String message) {
		this.lblInfo.setText(message);
	}

	public void setLblIdSpace(String space) {
		this.lblIdSpace.setText(space);
	}
}
