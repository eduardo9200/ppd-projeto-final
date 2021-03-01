package rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Registrador {

	public static String REFERENCIA = "SendMsgRef";
	
	private static Registry registry;
	
	/*
	 * Registra o serviço RMI/RPC
	 * */
	public static void registrarServidorRmi(int port) {
		try {
			ServidorRmi servidorRmi = new ServidorRmi();
			//Naming.rebind("//localhost/SendMsgRef", servidorRmi);
			
			registry = LocateRegistry.createRegistry(port);
			registry.bind(REFERENCIA, servidorRmi);

			System.out.println("Servidor RMI/RPC registrado com sucesso");
			
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}
