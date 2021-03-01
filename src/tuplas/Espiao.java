package tuplas;

import net.jini.core.entry.Entry;

/*
 * Tupla Espiao, que recebe e processa uma mensagem enviada entre usuários.
 * */
public class Espiao implements Entry {

	private static final long serialVersionUID = 1L;
	
	public Mensagem mensagem;
	
	public Espiao() { }
}
