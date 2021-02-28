package service;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import tuplas.Espiao;
import tuplas.Mensagem;
import tuplas.Usuario;

public class TuplaService {
	
	public static long TEMPO_VIDA_TUPLA = Lease.FOREVER;
	public static long TEMPO_MAX_LEITURA = 5_000;
	
	/*public static void testar(JavaSpace space) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException {
		System.out.println("Testando");
		Usuario usuario = new Usuario();
		usuario.nome = "teste";
		
		space.write(usuario, null, TEMPO_VIDA_TUPLA);
		
		Mensagem mensagem = new Mensagem();
		mensagem.remetente = usuario;
		mensagem.destinatario = usuario;
		mensagem.mensagem = "oi";
		
		space.write(mensagem, null, TEMPO_VIDA_TUPLA);
		
		Mensagem msg = (Mensagem) space.read(mensagem, null, TEMPO_MAX_LEITURA);
		System.out.println(msg.mensagem);
		
		Usuario template = new Usuario();
		
		Usuario user = (Usuario) space.read(template, null, TEMPO_MAX_LEITURA);
		System.out.println(user.nome);
	}*/
	
	public static boolean criaUsuario(String nome, JavaSpace space) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException {
		Usuario usuario = new Usuario();
		usuario.nome = nome;
		
		if(existeTemplate(usuario, space)) {
			return false;
		} else {
			space.write(usuario, null, TEMPO_VIDA_TUPLA);
			return true;
		}
	}
	
	public static void enviaMensagemParaEspiao(String nomeRemetente, String nomeDestinataio, String mensagem, JavaSpace space) throws RemoteException, TransactionException {
		Usuario usuarioRemetente = new Usuario();
		usuarioRemetente.nome = nomeRemetente;
		
		Usuario usuarioDestinatario = new Usuario();
		usuarioDestinatario.nome = nomeDestinataio;
		
		Mensagem msg = new Mensagem();
		msg.remetente = usuarioRemetente;
		msg.destinatario = usuarioDestinatario;
		msg.mensagem = mensagem;
		msg.dataHora = LocalDateTime.now();
		
		Espiao espiao = new Espiao();
		espiao.mensagem = msg;
		
		space.write(espiao, null, TEMPO_VIDA_TUPLA);
	}
	
	public static void enviaMensagem(Mensagem mensagem, JavaSpace space) throws RemoteException, TransactionException {
		space.write(mensagem, null, TEMPO_VIDA_TUPLA);
	}
	
	public static Mensagem buscaMensagem(String nomeRemetente, String nomeDestinatario, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Usuario usuarioRemetente = new Usuario();
		usuarioRemetente.nome = nomeRemetente;
		
		Usuario usuarioDestinatario = new Usuario();
		usuarioDestinatario.nome = nomeDestinatario;
		
		Mensagem template = new Mensagem();
		template.remetente = usuarioRemetente;
		template.destinatario = usuarioDestinatario;
		
		Mensagem msg = (Mensagem) space.take(template, null, TEMPO_MAX_LEITURA);
		
		return msg;
	}
	
	public static Espiao buscaMensagem(JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Espiao espiao = new Espiao();
		Espiao e = (Espiao) space.take(espiao, null, TEMPO_MAX_LEITURA);
		return e;
	}
	
	public static List<Usuario> buscaTodosUsuarios(JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		Usuario template = new Usuario();
		
		boolean existeUsuario = true;
		
		while(existeUsuario) {
			Usuario result = (Usuario) space.take(template, null, TEMPO_MAX_LEITURA);
			
			if(result != null)
				usuarios.add(result);
			else
				existeUsuario = false;
		}
		
		if(usuarios != null && !usuarios.isEmpty())
			for(Usuario u : usuarios)
				space.write(u, null, TEMPO_VIDA_TUPLA);
		
		return usuarios;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> boolean existeTemplate(T template, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		T result = null;
		
		if(template instanceof Usuario)
			result = (T) space.read((Usuario)template, null, TEMPO_MAX_LEITURA);
		
		else if(template instanceof Mensagem)
			result = (T) space.read((Mensagem)template, null, TEMPO_MAX_LEITURA);
		
		else if(template instanceof Espiao)
			result = (T) space.read((Espiao)template, null, TEMPO_MAX_LEITURA);
		
		return result != null;
	}
}
