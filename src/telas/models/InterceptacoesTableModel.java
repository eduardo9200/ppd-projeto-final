package telas.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import tuplas.Mensagem;

public class InterceptacoesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private List<String> palavrasSuspeitas = new ArrayList<String>();
	
	private List<Mensagem> dados = new ArrayList<Mensagem>(); //Linhas
	private String[] colunas = {"#", "Data", "Remetente", "Destinatário", "Mensagem", "Suspeita"}; //Colunas

	@Override
	public int getRowCount() {
		return dados.size();
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return colunas[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return dados.get(rowIndex).dataHora.toString();
		case 2:
			return dados.get(rowIndex).remetente.nome;
		case 3:
			return dados.get(rowIndex).destinatario.nome;
		case 4:
			return dados.get(rowIndex).mensagem;
		case 5:
			return this.palavrasSuspeitas.get(rowIndex);
		default:
			return null;
		}
	}
	
	public void addRow(Mensagem mensagem, String suspeita) {
		this.palavrasSuspeitas.add(suspeita);
		this.dados.add(mensagem);
		this.fireTableDataChanged();
	}

}
