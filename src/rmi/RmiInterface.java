package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import tuplas.Mensagem;

public interface RmiInterface extends Remote {

	public void sendMessageToTopic(Mensagem msg) throws RemoteException;
}
