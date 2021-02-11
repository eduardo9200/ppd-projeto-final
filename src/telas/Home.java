package telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.jini.space.JavaSpace;
import tuplas.Usuario;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Home extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JavaSpace space;
	private String nomeUsuario;
	
	private JPanel contentPane;
	private JPanel panelUsuarios;
	private JScrollPane scrollPaneUsuarios;
	private JList<Usuario> listUsuarios;
	
	private JButton btnConversar;
	private JButton btnAtualizar;
	
	private JLabel lblNomeUsuario;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home(null, null);
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
	public Home(String nomeUsuario, JavaSpace space) {		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 250, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		initComponents();
		initActions();
		
		this.space = space;
		this.nomeUsuario = nomeUsuario;
		this.lblNomeUsuario.setText(nomeUsuario);
	}
	
	private void initComponents() {
		panelUsuarios = new JPanel();
		panelUsuarios.setBounds(10, 35, 208, 223);
		contentPane.add(panelUsuarios);
		panelUsuarios.setLayout(new BorderLayout(0, 0));
		
		scrollPaneUsuarios = new JScrollPane();
		panelUsuarios.add(scrollPaneUsuarios, BorderLayout.CENTER);
		
		listUsuarios = new JList<Usuario>();
		scrollPaneUsuarios.setViewportView(listUsuarios);
		
		btnConversar = new JButton("Conversar");
		btnConversar.setBounds(122, 269, 96, 23);
		contentPane.add(btnConversar);
		
		btnAtualizar = new JButton("Atualizar");
		btnAtualizar.setBounds(10, 269, 96, 23);
		contentPane.add(btnAtualizar);
		
		lblNomeUsuario = new JLabel("");
		lblNomeUsuario.setBounds(10, 11, 208, 14);
		contentPane.add(lblNomeUsuario);
	}
	
	private void initActions() {
		btnConversar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConversarActionPerformed(e);
			}
		});
		
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAtualizarActionPerformed(e);
			}
		});
	}
	
	private void btnConversarActionPerformed(ActionEvent e) {
		
	}
	
	private void btnAtualizarActionPerformed(ActionEvent e) {
		
	}
}
