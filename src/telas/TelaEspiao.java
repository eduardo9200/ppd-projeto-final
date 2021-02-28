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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.initComponents();
		this.initActions();
		
		this.space = space;
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
	
	private void btnAdicionarPalavraActionPerformed(ActionEvent e) {
		String palavra = JOptionPane.showInputDialog(this, "Nova Palavra Suspeita");
		if(palavra != null && !palavra.isEmpty() && !palavra.isBlank()) {
			this.palavrasSuspeitas.add(palavra);
			this.palavrasSuspeitasTableModel.addRow(palavra);
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				Espiao espiao = TuplaService.buscaMensagem(space);
				
				if(espiao != null) {
					String suspeitaEncontrada = buscaSuspeita(espiao.mensagem);
					
					if(suspeitaEncontrada != null) {
						this.interceptacoesTableModel.addRow(espiao.mensagem, suspeitaEncontrada);
						enviaParaBroker(espiao.mensagem);
					}
					
					TuplaService.enviaMensagem(espiao.mensagem, space);
				}
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
	
	private String buscaSuspeita(Mensagem msg) {
		String existeSuspeita = null;
		
		if(this.palavrasSuspeitas != null && !this.palavrasSuspeitas.isEmpty())
			for(String suspeita : palavrasSuspeitas)
				if(msg.mensagem.contains(suspeita))
					existeSuspeita = suspeita;
					
		return existeSuspeita;
	}
	
	private void enviaParaBroker(Mensagem mensagem) {
		//FIXME: implementar este método;
	}
}
