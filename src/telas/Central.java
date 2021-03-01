package telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import service.TuplaService;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Central extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JavaSpace space;
	
	private JPanel contentPane;
	private JButton btnMediador;
	private JButton btnNovoUsuario;
	private JButton btnEspiao;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Central frame = new Central();
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
	public Central(JavaSpace space) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 200, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.initComponents();
		this.initActions();
		
		this.space = space;
	}

	private void initComponents() {
		btnMediador = new JButton("Abrir Mediador");
		btnMediador.setBounds(28, 90, 128, 23);
		contentPane.add(btnMediador);
		
		btnNovoUsuario = new JButton("Novo Usu\u00E1rio");
		btnNovoUsuario.setBounds(28, 131, 128, 23);
		contentPane.add(btnNovoUsuario);
		
		btnEspiao = new JButton("Abrir Espi\u00E3o");
		btnEspiao.setBounds(28, 45, 128, 23);
		contentPane.add(btnEspiao);
		
		lblNewLabel = new JLabel("Siga a sequ\u00EAncia abaixo");
		lblNewLabel.setBounds(10, 11, 164, 23);
		contentPane.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("1");
		lblNewLabel_1.setBounds(10, 49, 19, 14);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("2");
		lblNewLabel_2.setBounds(10, 94, 19, 14);
		contentPane.add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("3");
		lblNewLabel_3.setBounds(10, 135, 19, 14);
		contentPane.add(lblNewLabel_3);
	}
	
	private void initActions() {
		btnNovoUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNovoUsuarioActionPerformed(e);
			}
		});
		
		btnMediador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnMediadorActionPerformed(e);
			}
		});
		
		btnEspiao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEspiaoActionPerformed(e);
			}
		});
	}
	
	private void btnNovoUsuarioActionPerformed(ActionEvent e) {
		try {
			String nomeUsuario = criaUsuario();
			Home telaHome = new Home(nomeUsuario, space);
			telaHome.setVisible(true);
		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void btnMediadorActionPerformed(ActionEvent e) {
		try {
			Mediador mediador = new Mediador();
			mediador.setVisible(true);
			mediador.inscreverMediadorNoTopico();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void btnEspiaoActionPerformed(ActionEvent e) {
		try {
			TelaEspiao espiao = new TelaEspiao(space);
			espiao.setVisible(true);
			espiao.registrarServidorRmi();
			new Thread(espiao).start();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String criaUsuario() throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		String nomeUsuario = "";
		boolean nomeInvalido = true;
		
		while(nomeInvalido) {
			nomeUsuario = JOptionPane.showInputDialog(null, "Digite seu nome");
			
			//Clicou em cancelar
			if(nomeUsuario == null) {
				System.exit(0);
			}
			
			if(nomeUsuario.isEmpty() || nomeUsuario.isBlank()) {
				JOptionPane.showMessageDialog(null, "Nome vazio. Digite um nome.");
			
			} else {
				nomeInvalido = !TuplaService.criaUsuario(nomeUsuario, space);
				
				if(nomeInvalido)
					JOptionPane.showMessageDialog(null, "Já existe um usuário chamado " + nomeUsuario + ". Digite outro nome.");
			}
		}
		return nomeUsuario;
	}
}
