package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import tuplas.Mensagem;

public class ServidorRmi extends UnicastRemoteObject implements RmiInterface {

	private static final long serialVersionUID = 1L;

	protected ServidorRmi() throws RemoteException {
		super();
	}

	@Override
	public void sendMessageToTopic(Mensagem msg) throws RemoteException {
		//FIXME: implementar este método;
	}

}
