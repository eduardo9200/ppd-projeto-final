package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import tuplas.Mensagem;

public class ClienteRmi {

	/*
	 * Busca o serviço RMI/RPC registrado previamente e envia uma mensagem pelo método da interface RmiInterface
	 * */
	public static void execute(Mensagem msg, String host, int port) {
		try {
			RmiInterface rmi = (RmiInterface) Naming.lookup("rmi://" + host + ":" + port + "/" + Registrador.REFERENCIA);
			System.out.println("Objeto RMI/RPC Localizado");
			
			rmi.sendMessageToTopic(msg);
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
