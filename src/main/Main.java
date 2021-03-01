package main;

import javax.swing.JOptionPane;

import net.jini.space.JavaSpace;

import telas.Central;
import telas.Info;

public class Main {
	
	private static JavaSpace space;

	public static void main(String[] args) {
		try {
			JOptionPane.showMessageDialog(null, "Não esqueça de iniciar os serviços do Espaço de Tuplas e do ActiveMQ.");
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
			
			chamaTelaCentral(space);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void chamaTelaCentral(JavaSpace space) {
		try {
			Central central = new Central(space);
			central.setVisible(true);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
