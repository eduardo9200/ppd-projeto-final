package tuplas;

import java.util.ArrayList;
import java.util.List;

import net.jini.core.entry.Entry;
import net.jini.space.JavaSpace;

public class Espiao implements Entry {

	private static final long serialVersionUID = 1L;
	
	public List<String> palavrasSuspeitas;
	
	public Espiao() { }
	
	public Espiao(JavaSpace space) {
		this.palavrasSuspeitas = new ArrayList<String>();
		
		while(true) {
			
		}
	}
	
	public void adicionarPalavraRestrita(String palavra) {
		this.palavrasSuspeitas.add(palavra);
	}
	
	public boolean identificaPalavraRestrita(String frase) {
		boolean existeSuspeita = false;
		if(palavrasSuspeitas != null && !palavrasSuspeitas.isEmpty()) {
			for(String palavra : palavrasSuspeitas) {
				if(frase.contains(palavra)) {
					existeSuspeita = true;
					//Envia mensagem para o mediador via RMI/RPC
				}
			}	
		}
		return existeSuspeita;
	}
}
