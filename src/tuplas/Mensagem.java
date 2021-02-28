package tuplas;

import java.time.LocalDateTime;

import net.jini.core.entry.Entry;

public class Mensagem implements Entry {

	private static final long serialVersionUID = 1L;
	
	public Usuario remetente;
	public Usuario destinatario;
	public String mensagem;
	public LocalDateTime dataHora;
	
	public Mensagem() { }
}
