package telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import midware.Subscriber;
import telas.models.MediadorTableModel;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Mediador extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private MediadorTableModel mediadorTableModel = new MediadorTableModel();
	
	private JPanel contentPane;
	private JTable table;
	private JButton btnFechar;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mediador frame = new Mediador();
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
	public Mediador() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 490, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.initComponents();
		this.initActions();
	}
	
	private void initComponents() {
		btnFechar = new JButton("Fechar");
		btnFechar.setBounds(375, 377, 89, 23);
		contentPane.add(btnFechar);
		
		panel = new JPanel();
		panel.setBounds(10, 43, 454, 323);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(mediadorTableModel);
		table.setAutoCreateRowSorter(true);
		
		lblNewLabel = new JLabel("Mediador");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 11, 136, 21);
		contentPane.add(lblNewLabel);
	}
	
	private void initActions() {
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnFecharActionPerformed(e);
			}
		});
	}
	
	private void btnFecharActionPerformed(ActionEvent e) {
		this.dispose();
	}
	
	/*
	 * Inscreve o mediador no tópico. Função chamada pela Central.java.
	 * */
	public void inscreverMediadorNoTopico() {
		new Subscriber(this).execute();
	}
	
	/*
	 * Adiciona a mensagem vinda do tópico na tela do mediador.
	 * */
	public void adicionarLinhaNaTabela(String texto) {
		this.mediadorTableModel.addRow(texto);
	}
}
