package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import tuplas.Mensagem;

public class ClienteRmi {

	public static void execute(Mensagem msg) {
		try {
			RmiInterface rmi = (RmiInterface) Naming.lookup("//localhost/SendMsgRef");
			System.out.println("Objeto Localizado");
			
			rmi.sendMessageToTopic(msg);
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
