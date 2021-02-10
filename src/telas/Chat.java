package telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Chat extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField textFieldMensagem;
	
	private JButton btnEnviar;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel lblNome;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat();
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
	public Chat() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		initComponents();
		initActions();
	}
	
	private void initComponents() {
		textFieldMensagem = new JTextField();
		textFieldMensagem.setBounds(10, 344, 234, 26);
		contentPane.add(textFieldMensagem);
		textFieldMensagem.setColumns(10);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(254, 346, 70, 23);
		contentPane.add(btnEnviar);
		
		panel = new JPanel();
		panel.setBounds(10, 34, 314, 299);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		lblNome = new JLabel("");
		lblNome.setBounds(10, 0, 120, 23);
		contentPane.add(lblNome);
	}
	
	private void initActions() {
		textFieldMensagem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					enviaMensagem();
				}
			}
		});
		
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviaMensagem();
			}
		});
	}
	
	private void enviaMensagem() {
		String text = textFieldMensagem.getText();
		
		if(text != null && !text.isEmpty() && !text.isBlank()) {
			textArea.append(text + "\n");	
		}
		
		textFieldMensagem.setText("");
	}
}
