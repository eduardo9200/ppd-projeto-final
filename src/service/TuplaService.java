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

/*
 * Servi�o para implementa��es de CRUD no espa�o de tuplas.
 * */
public class TuplaService {
	
	public static long TEMPO_VIDA_TUPLA = Lease.FOREVER; //Tempo de vida da tupla dentro do espa�o de tuplas.
	public static long TEMPO_MAX_LEITURA = 5_000; //Tempo m�ximo para as fun��es read e take, na hora de buscar uma tupla no espa�o.
	
	/*
	 * Cria uma tupla de usu�rio no espa�o de tuplas.
	 * 
	 * @param nome o nome do usu�rio que se quer criar.
	 * @param space refer�ncia ao servi�o do JavaSpace.
	 * @return {@code true}, se o usu�rio foi criado; caso contr�rio, {@code false};
	 * */
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
	
	/*
	 * Quando o usu�rio envia uma mensagem para outro, ela ser� armazenada em uma tupla especial, denominada Espi�o,
	 * que far� uma verifica��o dessa mensagem, na busca de palavras ou termos suspeitos, e ent�o envia-la-� para o destino.
	 * 
	 * @param nomeRemetente nome do remetente.
	 * @param nomeDestinatario nome do destinat�rio.
	 * @param mensagem mensagem enviada do remetente para o destinat�rio.
	 * @param space refer�ncia ao servi�o do JavaSpace.
	 * */
	public static void enviaMensagemParaEspiao(String nomeRemetente, String nomeDestinataio, String mensagem, JavaSpace space) throws RemoteException, TransactionException {
		//cria usu�rio remetente
		Usuario usuarioRemetente = new Usuario();
		usuarioRemetente.nome = nomeRemetente;
		
		//cria usu�rio destinat�rio
		Usuario usuarioDestinatario = new Usuario();
		usuarioDestinatario.nome = nomeDestinataio;
		
		//seta os campos da tupla de mensagem
		Mensagem msg = new Mensagem();
		msg.remetente = usuarioRemetente;
		msg.destinatario = usuarioDestinatario;
		msg.mensagem = mensagem;
		msg.dataHora = LocalDateTime.now();
		
		//insere a mensagem criada na tupla espi�o
		Espiao espiao = new Espiao();
		espiao.mensagem = msg;
		
		//insere o espi�o no espa�o, para ser capturado pela TelaEspiao
		space.write(espiao, null, TEMPO_VIDA_TUPLA);
	}
	
	/*
	 * M�todo utilizado pelo Espi�o para enviar uma mensagem para o espa�o de tuplas,
	 * ap�s process�-la, e ela possa chegar ao destinat�rio.
	 * 
	 * @param mensagem mensagem processada pelo espi�o e que ser� enviada ao seu destinat�rio.
	 * @param space refer�ncia ao servi�o do JavaSpace.
	 * */
	public static void enviaMensagem(Mensagem mensagem, JavaSpace space) throws RemoteException, TransactionException {
		space.write(mensagem, null, TEMPO_VIDA_TUPLA);
	}
	
	/*
	 * Busca de mensagens pelos usu�rios.
	 * Busca uma mensagem no espa�o para viabilizar a troca de mensagens entre usu�rios.
	 * Busca, inclusive, mensagens enviadas offline.
	 * 
	 * @param nomeRemetente nome do remetente.
	 * @param nomeDestinatario nome do destinat�rio.
	 * @para space refer�ncia ao servi�o do JavaSpace.
	 * @return a tupla Mensagem, se ela for localizada; {@code null}, caso contr�rio.
	 * */
	public static Mensagem buscaMensagem(String nomeRemetente, String nomeDestinatario, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		//cria usu�rio remetente
		Usuario usuarioRemetente = new Usuario();
		usuarioRemetente.nome = nomeRemetente;
		
		//cria usu�rio destinat�rio
		Usuario usuarioDestinatario = new Usuario();
		usuarioDestinatario.nome = nomeDestinatario;
		
		//cria o template de Mensagem a ser buscado
		Mensagem template = new Mensagem();
		template.remetente = usuarioRemetente;
		template.destinatario = usuarioDestinatario;
		
		//realiza a busca e retorna o resultado
		Mensagem msg = (Mensagem) space.take(template, null, TEMPO_MAX_LEITURA);
		return msg;
	}
	
	/*
	 * Busca de mensagens pelo Espi�o.
	 * Busca uma mensagem no espa�o que foi direcionada ao espi�o.
	 * 
	 * @param space refer�ncia ao servi�o do JavaSpace.
	 * @return uma tupla Espi�o, caso exista; {@code null}, caso contr�rio.
	 * */
	public static Espiao buscaMensagem(JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Espiao espiao = new Espiao();
		Espiao e = (Espiao) space.take(espiao, null, TEMPO_MAX_LEITURA);
		return e;
	}
	
	/*
	 * Lista todos as tuplas de Usu�rios presentes no espa�o e as armazena em uma lista.
	 * 
	 * @param space refer�ncia ao servi�o do JavaSpace.
	 * @return uma lista de tuplas Usu�rio.
	 * */
	public static List<Usuario> buscaTodosUsuarios(JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		Usuario template = new Usuario();
		
		boolean existeUsuario = true;
		
		//remove todas as tuplas de Usu�rio e as armazena na lista usu�rios
		while(existeUsuario) {
			Usuario result = (Usuario) space.take(template, null, TEMPO_MAX_LEITURA);
			
			if(result != null)
				usuarios.add(result);
			else
				existeUsuario = false;
		}
		
		//devolve as tuplas removidas para o espa�o
		if(usuarios != null && !usuarios.isEmpty())
			for(Usuario u : usuarios)
				space.write(u, null, TEMPO_VIDA_TUPLA);
		
		//retorna a lista de usu�rios presentes no espa�o
		return usuarios;
	}
	
	/*
	 * Busca um determinado template no espa�o de tuplas e verifica se ele existe.
	 * 
	 * @param template um valor gen�rico representando o template a ser buscado.
	 * @param space refer�ncia ao servi�o do JavaSpace.
	 * @return {@code true} se o template existe; caso contr�rio, {@code false}.
	 * */
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
