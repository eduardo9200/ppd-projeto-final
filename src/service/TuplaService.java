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
 * Serviço para implementações de CRUD no espaço de tuplas.
 * */
public class TuplaService {
	
	public static long TEMPO_VIDA_TUPLA = Lease.FOREVER; //Tempo de vida da tupla dentro do espaço de tuplas.
	public static long TEMPO_MAX_LEITURA = 5_000; //Tempo máximo para as funções read e take, na hora de buscar uma tupla no espaço.
	
	/*
	 * Cria uma tupla de usuário no espaço de tuplas.
	 * 
	 * @param nome o nome do usuário que se quer criar.
	 * @param space referência ao serviço do JavaSpace.
	 * @return {@code true}, se o usuário foi criado; caso contrário, {@code false};
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
	 * Quando o usuário envia uma mensagem para outro, ela será armazenada em uma tupla especial, denominada Espião,
	 * que fará uma verificação dessa mensagem, na busca de palavras ou termos suspeitos, e então envia-la-á para o destino.
	 * 
	 * @param nomeRemetente nome do remetente.
	 * @param nomeDestinatario nome do destinatário.
	 * @param mensagem mensagem enviada do remetente para o destinatário.
	 * @param space referência ao serviço do JavaSpace.
	 * */
	public static void enviaMensagemParaEspiao(String nomeRemetente, String nomeDestinataio, String mensagem, JavaSpace space) throws RemoteException, TransactionException {
		//cria usuário remetente
		Usuario usuarioRemetente = new Usuario();
		usuarioRemetente.nome = nomeRemetente;
		
		//cria usuário destinatário
		Usuario usuarioDestinatario = new Usuario();
		usuarioDestinatario.nome = nomeDestinataio;
		
		//seta os campos da tupla de mensagem
		Mensagem msg = new Mensagem();
		msg.remetente = usuarioRemetente;
		msg.destinatario = usuarioDestinatario;
		msg.mensagem = mensagem;
		msg.dataHora = LocalDateTime.now();
		
		//insere a mensagem criada na tupla espião
		Espiao espiao = new Espiao();
		espiao.mensagem = msg;
		
		//insere o espião no espaço, para ser capturado pela TelaEspiao
		space.write(espiao, null, TEMPO_VIDA_TUPLA);
	}
	
	/*
	 * Método utilizado pelo Espião para enviar uma mensagem para o espaço de tuplas,
	 * após processá-la, e ela possa chegar ao destinatário.
	 * 
	 * @param mensagem mensagem processada pelo espião e que será enviada ao seu destinatário.
	 * @param space referência ao serviço do JavaSpace.
	 * */
	public static void enviaMensagem(Mensagem mensagem, JavaSpace space) throws RemoteException, TransactionException {
		space.write(mensagem, null, TEMPO_VIDA_TUPLA);
	}
	
	/*
	 * Busca de mensagens pelos usuários.
	 * Busca uma mensagem no espaço para viabilizar a troca de mensagens entre usuários.
	 * Busca, inclusive, mensagens enviadas offline.
	 * 
	 * @param nomeRemetente nome do remetente.
	 * @param nomeDestinatario nome do destinatário.
	 * @para space referência ao serviço do JavaSpace.
	 * @return a tupla Mensagem, se ela for localizada; {@code null}, caso contrário.
	 * */
	public static Mensagem buscaMensagem(String nomeRemetente, String nomeDestinatario, JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		//cria usuário remetente
		Usuario usuarioRemetente = new Usuario();
		usuarioRemetente.nome = nomeRemetente;
		
		//cria usuário destinatário
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
	 * Busca de mensagens pelo Espião.
	 * Busca uma mensagem no espaço que foi direcionada ao espião.
	 * 
	 * @param space referência ao serviço do JavaSpace.
	 * @return uma tupla Espião, caso exista; {@code null}, caso contrário.
	 * */
	public static Espiao buscaMensagem(JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		Espiao espiao = new Espiao();
		Espiao e = (Espiao) space.take(espiao, null, TEMPO_MAX_LEITURA);
		return e;
	}
	
	/*
	 * Lista todos as tuplas de Usuários presentes no espaço e as armazena em uma lista.
	 * 
	 * @param space referência ao serviço do JavaSpace.
	 * @return uma lista de tuplas Usuário.
	 * */
	public static List<Usuario> buscaTodosUsuarios(JavaSpace space) throws RemoteException, UnusableEntryException, TransactionException, InterruptedException {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		Usuario template = new Usuario();
		
		boolean existeUsuario = true;
		
		//remove todas as tuplas de Usuário e as armazena na lista usuários
		while(existeUsuario) {
			Usuario result = (Usuario) space.take(template, null, TEMPO_MAX_LEITURA);
			
			if(result != null)
				usuarios.add(result);
			else
				existeUsuario = false;
		}
		
		//devolve as tuplas removidas para o espaço
		if(usuarios != null && !usuarios.isEmpty())
			for(Usuario u : usuarios)
				space.write(u, null, TEMPO_VIDA_TUPLA);
		
		//retorna a lista de usuários presentes no espaço
		return usuarios;
	}
	
	/*
	 * Busca um determinado template no espaço de tuplas e verifica se ele existe.
	 * 
	 * @param template um valor genérico representando o template a ser buscado.
	 * @param space referência ao serviço do JavaSpace.
	 * @return {@code true} se o template existe; caso contrário, {@code false}.
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
