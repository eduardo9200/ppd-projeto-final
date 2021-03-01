package telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import service.TuplaService;
import tuplas.Usuario;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Home extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JavaSpace space;
	private String meuNomeUsuario;
	
	private JPanel contentPane;
	private JPanel panelUsuarios;
	private JScrollPane scrollPaneUsuarios;
	private JList<String> listUsuarios;
	
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
	public Home(String meuNomeUsuario, JavaSpace space) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		initComponents();
		initActions();
		
		this.space = space;
		this.meuNomeUsuario = meuNomeUsuario;
		this.lblNomeUsuario.setText(meuNomeUsuario);
	}
	
	private void initComponents() {
		panelUsuarios = new JPanel();
		panelUsuarios.setBounds(10, 35, 208, 223);
		contentPane.add(panelUsuarios);
		panelUsuarios.setLayout(new BorderLayout(0, 0));
		
		scrollPaneUsuarios = new JScrollPane();
		panelUsuarios.add(scrollPaneUsuarios, BorderLayout.CENTER);
		
		listUsuarios = new JList<String>();
		scrollPaneUsuarios.setViewportView(listUsuarios);
		listUsuarios.setBorder(BorderFactory.createTitledBorder("Usu�rios"));
		listUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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
	
	/*
	 * Abre a conversa entre usu�rios em uma tela de chat, de acordo com o usu�rio selecionado na tela.
	 * */
	private void btnConversarActionPerformed(ActionEvent e) {
		if(this.listUsuarios.getSelectedIndex() != -1) {
			String nomeUsuarioQueQueroConversar = this.listUsuarios.getSelectedValue().toString();
			Chat chat = new Chat(meuNomeUsuario, nomeUsuarioQueQueroConversar, this.space);
			chat.setVisible(true);
			new Thread(chat).start();
		}
	}
	
	/*
	 * Atualiza a lista de usu�rios existentes no espa�o de tuplas.
	 * */
	private void btnAtualizarActionPerformed(ActionEvent e) {
		try {
			this.atualizarListaUsuarios();
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	/*
	 * Busca a lista de usu�rios existentes no espa�o de tuplas para mostrar na tela do usu�rio.
	 * */
	private void atualizarListaUsuarios() throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		List<String> nomesUsuarios = new ArrayList<String>();
		
		//Busca todos os usu�rios no espa�o.
		List<Usuario> usuarios = TuplaService.buscaTodosUsuarios(space);
		
		//Mostra a lista de usu�rios no espa�o, excluindo o pr�prio usu�rio.
		if(usuarios != null && !usuarios.isEmpty()) {
			for(Usuario u : usuarios)
				if(!u.nome.equals(meuNomeUsuario))
					nomesUsuarios.add(u.nome);
			
			//Mostra o resultado da busca na tela do usu�rio.
			listUsuarios.setListData(nomesUsuarios.toArray(new String[nomesUsuarios.size()]));
		}
	}
}
