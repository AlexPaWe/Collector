package de.alexpawe.collector.objects;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Table {

	private List<String[]> rows;
	private String[] header;
	
	
	public Table(Set<String> header) {
		this.header = new String[header.size()];
		int i = 0;
		for (String element : header) {
			this.header[i] = element;
			i++;
		}
		
		rows = new LinkedList<String[]>();
	}
	
	public void addRow(Map<String, String> inputrow) {
		String[] row = new String[header.length];
		for (int i = 0; i < header.length; i++) {
			if (inputrow.containsKey(header[i])) {
				row[i] = inputrow.get(header[i]);
			} else {
				row[i] = "nil";							// TODO: Find a symbol for an empty field.
			}
		}
		rows.add(row);
	}
	
	public void addRows(List<Map<String, String>> inputrows) {
		for (Map<String, String> row : inputrows) {
			this.addRow(row);
		}
	}
	
	public String toString() {
		String headerstring = "";
		for (int h = 0; h < header.length; h++) {
			headerstring = headerstring.concat(header[h]);
			if (h != header.length - 1) {
				headerstring = headerstring.concat(",");
			}
		}
		headerstring = headerstring.concat("\n");
		//System.out.println(headerstring);
		
		String rowstring = "";
		for (String[] row : rows) {
			for (int i = 0; i < row.length; i++) {
				rowstring = rowstring.concat(row[i]);
				if (i != row.length - 1) {
					rowstring = rowstring.concat(",");
				}
			}
			rowstring = rowstring.concat("\n");
		}
		
		return headerstring.concat(rowstring);
	}
	
}
