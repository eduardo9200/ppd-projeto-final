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
import tuplas.Mensagem;
import tuplas.Usuario;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

public class Chat extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private JavaSpace space;
	
	private String meuNome;
	private String nomeOutro;
	
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
	/*public static void main(String[] args) {
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
	}*/

	/**
	 * Create the frame.
	 */
	public Chat(String meuNome, String nomeUsuarioQueQueroConversar, JavaSpace space) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		initComponents();
		initActions();
	
		this.space = space;
		this.meuNome = meuNome;
		this.nomeOutro = nomeUsuarioQueQueroConversar;
		this.lblNome.setText("Eu: " + meuNome + " >> " + nomeOutro);
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
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		
		lblNome = new JLabel("");
		lblNome.setBounds(10, 11, 314, 14);
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
		String mensagem = textFieldMensagem.getText();
		
		if(mensagem != null && !mensagem.isEmpty() && !mensagem.isBlank()) {
			textArea.append("Eu: " + mensagem + "\n");
			
			try {
				TuplaService.enviaMensagemParaEspiao(meuNome, nomeOutro, mensagem, space);
			} catch (RemoteException | TransactionException e) {
				e.printStackTrace();
			}
		}
		
		textFieldMensagem.setText("");
	}

	@Override
	public void run() {
		while(true) {
			try {
				Mensagem msg = TuplaService.buscaMensagem(nomeOutro, meuNome, space);
				
				if(msg != null) {
					this.textArea.append(nomeOutro + ": " + msg.mensagem + "\n");
				}
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
}
