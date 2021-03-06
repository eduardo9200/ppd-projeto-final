package telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import rmi.ClienteRmi;
import rmi.Registrador;
import service.TuplaService;
import telas.models.InterceptacoesTableModel;
import telas.models.PalavrasSuspeitasTableModel;
import tuplas.Espiao;
import tuplas.Mensagem;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class TelaEspiao extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JPanel panelInterceptacoes;
	private JPanel panelPalavras;
	
	private JScrollPane scrollPaneInterceptacoes;
	private JScrollPane scrollPanePalavras;
	
	private JTable table;
	private JTable tablePalavras;
	
	private JButton btnAdicionarPalavra;
	
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	
	private String host;
	private int port;
	
	private JavaSpace space;
	private List<String> palavrasSuspeitas = new ArrayList<String>();
	private InterceptacoesTableModel interceptacoesTableModel = new InterceptacoesTableModel();
	private PalavrasSuspeitasTableModel palavrasSuspeitasTableModel = new PalavrasSuspeitasTableModel();

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaEspiao frame = new TelaEspiao();
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
	public TelaEspiao(JavaSpace space) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.initComponents();
		this.initActions();
		
		this.space = space;
	}
	
	/*
	 * Fun��o para registrar o servidor RMI/RPC em um host e uma porta,
	 * pois estava dando erro ao tentar fazer esse processo de modo autom�tico.
	 * */
	public void registrarServidorRmi() {
		String host = "";
		String port = "";
		
		while(host.isEmpty() || host.isBlank())
			host = JOptionPane.showInputDialog(this, "Informe um host para registrar o rmi. Ex.: localhost, 192.168.0.1, etc.");
		
		while(port.isEmpty() || port.isBlank())
			port = JOptionPane.showInputDialog(this, "Informe uma porta para registrar o rmi, entre 0 e 65535.");
		
		if(host == null || port == null) {
			JOptionPane.showMessageDialog(this, "N�o ser� poss�vel registrar o servi�o do rmi (host e/ou porta = null). Abra novamente a tela do Espi�o para registr�-lo.");
			return;
		}
		
		int porta = Integer.parseInt(port);
		
		Registrador.registrarServidorRmi(porta);
		
		this.port = porta;
		this.host = host;
	}
	
	private void initComponents() {
		panelInterceptacoes = new JPanel();
		panelInterceptacoes.setBounds(10, 63, 564, 177);
		contentPane.add(panelInterceptacoes);
		panelInterceptacoes.setLayout(new BorderLayout(0, 0));
		
		scrollPaneInterceptacoes = new JScrollPane();
		panelInterceptacoes.add(scrollPaneInterceptacoes, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPaneInterceptacoes.setViewportView(table);
		table.setModel(interceptacoesTableModel);
		table.setAutoCreateRowSorter(true);
		
		btnAdicionarPalavra = new JButton("Adicionar Palavra");
		btnAdicionarPalavra.setBounds(434, 327, 140, 23);
		contentPane.add(btnAdicionarPalavra);
		
		lblNewLabel = new JLabel("Intercepta\u00E7\u00F5es");
		lblNewLabel.setBounds(10, 38, 114, 14);
		contentPane.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Espi\u00E3o");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(10, 11, 94, 23);
		contentPane.add(lblNewLabel_1);
		
		panelPalavras = new JPanel();
		panelPalavras.setBounds(10, 275, 414, 141);
		contentPane.add(panelPalavras);
		panelPalavras.setLayout(new BorderLayout(0, 0));
		
		scrollPanePalavras = new JScrollPane();
		panelPalavras.add(scrollPanePalavras, BorderLayout.CENTER);
		
		tablePalavras = new JTable();
		scrollPanePalavras.setViewportView(tablePalavras);
		tablePalavras.setModel(palavrasSuspeitasTableModel);
		tablePalavras.setAutoCreateRowSorter(true);
		
		lblNewLabel_2 = new JLabel("Palavras Suspeitas");
		lblNewLabel_2.setBounds(10, 250, 138, 14);
		contentPane.add(lblNewLabel_2);
	}
	
	private void initActions() {
		btnAdicionarPalavra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAdicionarPalavraActionPerformed(e);
			}
		});
	}
	
	/*
	 * Adiciona uma palavra suspeita a ser buscada nas mensagens entre usu�rios.
	 * */
	private void btnAdicionarPalavraActionPerformed(ActionEvent e) {
		String palavra = JOptionPane.showInputDialog(this, "Nova Palavra Suspeita");
		if(palavra != null && !palavra.isEmpty() && !palavra.isBlank()) {
			this.palavrasSuspeitas.add(palavra);
			this.palavrasSuspeitasTableModel.addRow(palavra);
		}
	}

	/*
	 * Thread utilizada para buscar mensagens enviadas para o espi�o para serem analisadas.
	 * Ap�s a an�lise, ela � devolvida para o espa�o de tuplas para que seja recebida pelo destinat�rio.
	 * Se a suspeita for identificada, ent�o � registrada uma mensagem no t�pico para que seja lida pelo mediador.
	 * */
	@Override
	public void run() {
		while(true) {
			try {
				//Busca a mensagem a ser lida pelo espi�o.
				Espiao espiao = TuplaService.buscaMensagem(space);
				
				if(espiao != null) {
					//Devolve a mensagem para o espa�o, para ser recebida pelo destinat�rio.
					TuplaService.enviaMensagem(espiao.mensagem, space);
					
					//Busca palavra suspeita.
					String suspeitaEncontrada = buscaSuspeita(espiao.mensagem);
					
					//Se a suspeita existir, ent�o ela � enviada ao broker para regist�-la no t�pico.
					if(suspeitaEncontrada != null) {
						this.interceptacoesTableModel.addRow(espiao.mensagem, suspeitaEncontrada);
						enviaParaBroker(espiao.mensagem);
					}
				}
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
	
	/*
	 * Busca uma palavra suspeita em uma mensagem.
	 * Percorre a lista de palavras suspeitas e, para cada uma, verifica se ela est� contida na mensagem entre usu�rios.
	 * Se estiver, retorna a palavra suspeita localizada. Caso contr�rio, retorna null. 
	 * */
	private String buscaSuspeita(Mensagem msg) {
		String existeSuspeita = null;
		
		//Percorre a lista de palavras suspeitas e verifica se alguma delas est� contida na mensagem.
		if(this.palavrasSuspeitas != null && !this.palavrasSuspeitas.isEmpty())
			for(String suspeita : palavrasSuspeitas)
				if(msg.mensagem.contains(suspeita))
					existeSuspeita = suspeita;
					
		return existeSuspeita;
	}
	
	/*
	 * Chama o m�todo da interface RMI/RPC para enviar uma mensagem ao broker, que far� o registro no t�pico e ser� lida pelo mediador.
	 * */
	private void enviaParaBroker(Mensagem mensagem) {
		ClienteRmi.execute(mensagem, this.host, this.port);
	}
}
