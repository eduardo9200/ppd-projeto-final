package main;

import javax.swing.JOptionPane;

import net.jini.space.JavaSpace;

import telas.Central;
import telas.Info;

public class Main {
	
	private static JavaSpace space;

	public static void main(String[] args) {
		try {
			JOptionPane.showMessageDialog(null, "N�o esque�a de iniciar os servi�os do Espa�o de Tuplas e do ActiveMQ.");
			instanciaEspacoTuplasEIniciaSistema();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Instancia o espa�o de tuplas e starta o sistema, chamando o JFrame Central.java.
	 * */
	private static void instanciaEspacoTuplasEIniciaSistema() {
		try {
			Info infoFrame = new Info("Procurando pelo servi�o JavaSpace...");
			infoFrame.setVisible(true);
			
			Lookup finder = new Lookup(JavaSpace.class);
			space = (JavaSpace) finder.getService();
			
			if(space == null) {
				infoFrame.setLblInfo("O servi�o JavaSpace n�o foi encontrado. Encerrando...");
				infoFrame.dispose();
				System.exit(-1);
			}
			
			infoFrame.setLblInfo("O servi�o JavaSpace foi encontrado.");
			infoFrame.setLblIdSpace("ID space: " + space);
			System.out.println(space);
			
			//Ap�s encontrar o servi�o do JavaSpace, o repassa para a tela Central e a inicia.
			chamaTelaCentral(space);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Abre a tela Central para intera��o e uso do sistema.
	 * */
	private static void chamaTelaCentral(JavaSpace space) {
		try {
			Central central = new Central(space);
			central.setVisible(true);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
