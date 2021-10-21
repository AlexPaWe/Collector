package de.alexpawe.collector.objects;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Table {

	public static final String CSV_LIMITER = ",";
	
	public static final String PARAM_PRE  = "param-";
	public static final String METRIC_PRE = "metric-";
	
	private String[] header;
	private List<String[]> rows;
	
	
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
		
		String headerstring = getPrintHeader();
		//System.out.println(headerstring);
		
		String rowstring = "";
		for (String[] row : rows) {
			for (int i = 0; i < row.length; i++) {
				rowstring = rowstring.concat(row[i]);
				if (i != row.length - 1) {
					rowstring = rowstring.concat(CSV_LIMITER);
				}
			}
			rowstring = rowstring.concat("\n");
		}
		return headerstring.concat(rowstring);
	}
	
	public String getPrintHeader() {
		String printheader = "";
		for (int i = 0; i < header.length; i++) {
			if (Pattern.matches("[A-Z_]*", header[i])) {
				printheader = printheader.concat(PARAM_PRE + header[i]);
			} else {
				printheader = printheader.concat(METRIC_PRE + header[i]);
			}
			if (i != header.length - 1)
				printheader = printheader.concat(",");
		}
		return printheader.concat("\n");
	}
}
