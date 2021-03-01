package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import midware.Publisher;
import tuplas.Mensagem;

public class ServidorRmi extends UnicastRemoteObject implements RmiInterface {

	private static final long serialVersionUID = 1L;

	protected ServidorRmi() throws RemoteException {
		super();
	}

	@Override
	public void sendMessageToTopic(Mensagem msg) throws RemoteException {
		String msgToTopic = this.geraMensagem(msg);
		new Publisher(msgToTopic).execute();
	}

	private String geraMensagem(Mensagem msg) {
		return new StringBuilder()
			.append(msg.remetente.nome)
			.append(" enviou para ")
			.append(msg.destinatario.nome)
			.append(" uma mensagem suspeita.")
			.toString();
	}
}
