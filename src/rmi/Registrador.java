package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Registrador {

	public static void registrarServidor() {
		try {
			ServidorRmi obj = new ServidorRmi();
			Naming.rebind("//localhost/SendMsgRef", obj);
			System.out.println("Servidor registrado");
			
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
