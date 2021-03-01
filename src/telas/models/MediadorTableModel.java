package telas.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MediadorTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private List<String> dados = new ArrayList<String>();
	private String[] colunas = {"#", "Mensagem do Espião"};

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
			return dados.get(rowIndex);
		default:
			return null;
		}
	}
	
	public void addRow(String dado) {
		this.dados.add(dado);
		this.fireTableDataChanged();
	}

}
