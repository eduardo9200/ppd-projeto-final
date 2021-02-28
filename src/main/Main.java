package main;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import service.TuplaService;
import telas.Home;
import telas.Info;
import telas.TelaEspiao;

public class Main {
	
	private static JavaSpace space;

	public static void main(String[] args) {
		try {
			instanciaEspacoTuplasEIniciaSistema();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void instanciaEspacoTuplasEIniciaSistema() {
		try {
			Info infoFrame = new Info("Procurando pelo serviço JavaSpace...");
			infoFrame.setVisible(true);
			
			Lookup finder = new Lookup(JavaSpace.class);
			space = (JavaSpace) finder.getService();
			
			if(space == null) {
				infoFrame.setLblInfo("O serviço JavaSpace não foi encontrado. Encerrando...");
				infoFrame.dispose();
				System.exit(-1);
			}
			
			infoFrame.setLblInfo("O serviço JavaSpace foi encontrado.");
			infoFrame.setLblIdSpace("ID space: " + space);
			System.out.println(space);
			
			int option = JOptionPane.showConfirmDialog(null, "Entrar como Espião?", "Espião", JOptionPane.YES_NO_OPTION);
			
			if(option == -1) { //Clicou em Fechar
				System.exit(0);				
			} else if(option == 0) { //Clicou 'Sim'
				chamaInterfaceEspiao();				
			} else if(option == 1) { //Clicou 'Não'
				String nomeUsuario = criaUsuario();
				chamaTelaHome(nomeUsuario);				
			} else {
				throw new Exception("Opção Inválida");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String criaUsuario() throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
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
	
	private static void chamaInterfaceEspiao() {
		try {
			TelaEspiao espiao = new TelaEspiao(space);
			espiao.setVisible(true);
			new Thread(espiao).start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void chamaTelaHome(String nomeUsuario) {
		try {
			Home telaHome = new Home(nomeUsuario, space);
			telaHome.setVisible(true);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
