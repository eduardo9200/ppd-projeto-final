package tuplas;

import net.jini.core.entry.Entry;

//Verificar se é necessário criar essa tupla
public class Space implements Entry {

	private static final long serialVersionUID = 1L;

	public Usuario usuario;
	public Mensagem mensagem;
	public Espiao espiao;
	
	public Space() { }
}
