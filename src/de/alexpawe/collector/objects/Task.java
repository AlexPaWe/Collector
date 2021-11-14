package de.alexpawe.collector.objects;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Task {

	private String id;
	private Map<String, Object> parameters;
	private Map<Integer, Map<String, String>> iterations;
	
	private String[] metricsheader;
	private String[] tracepointheader;
	
	public Task(String id, JSONObject taskObj) {
		this.id = id;
		this.parameters = taskObj.toMap();
		
		addValues();
	}
	
	private void addValues() {
		iterations = new HashMap<Integer, Map<String, String>>();
		
		metricsheader = null;
		List<String[]> r = null;
		
		String combined_path = "results/" + id + "/combined.csv";
		try {
			CSVReader reader = new CSVReader(new FileReader(combined_path));
			r = reader.readAll();
			if (r.size() > 0) {
				metricsheader = r.get(0);
				//System.out.println(r.size());	// TODO: Remove
				for (int j = 1; j < r.size(); j++) {
					String[] line = r.get(j);
					//System.out.println(j + ".: " + line[line.length-1]);	// TODO: Remove
					Map<String, String> tmp = new HashMap<String, String>();
					if (metricsheader.length > 2) {
						for (int i = 0; i < line.length; i++) {
							//System.out.println(line[i]);
							//System.out.println("metricsheader[" + i + "] = " + metricsheader[i] + ";\n" + "line[" + i + "] = " + line[i]);
							tmp.put(metricsheader[i], line[i]);
							//System.out.println(tmp);
						}
					}
					iterations.put(j, tmp);
				}
			} else {
				System.out.println(combined_path + " is empty");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tracepointheader = null;
		List<String[]> s = null;
		
		System.out.println("Combined files read!");
		
		for (int iteration : iterations.keySet()) {
			String traces_path = "results/" + id + "/traces" + iteration + ".csv";
			try {
				CSVReader tracesReader = new CSVReader(new FileReader(traces_path));
				s = tracesReader.readAll();
				if (s.size() > 0) {
					tracepointheader = s.get(0);
					for (int k = 1; k < s.size(); k++) {
						String[] line = s.get(k);
						Map<String, String> tmptp = new HashMap<String, String>();
						for (int l = 0; l < line.length; l++) {
							tmptp.put(tracepointheader[l], line[l]);
						}
						iterations.get(iteration).putAll(tmptp);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CsvException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Set<String> getParameterKeys() {
		return parameters.keySet();
	}
	
	public String[] getMetricsheader() {
		return metricsheader;
	}
	
	public String[] getTracepointheader() {
		return tracepointheader;
	}
	
	public String getID() {
		return id;
	}
	
	public List<Map<String, String>> getRows() {
		List<Map<String, String>> rows = new LinkedList<Map<String, String>>(); 
		
		for (Entry<Integer, Map<String, String>> iteration : iterations.entrySet()) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("TASKID", id);
			row.put("Iteration", iteration.getKey().toString());
			
			for (Entry<String, Object> entry : parameters.entrySet()) {
				row.put(entry.getKey(), (String) entry.getValue());
			}
			
			Map<String, String> metrics = iteration.getValue();
			for (Entry<String, String> entry : metrics.entrySet()) {
				row.put(entry.getKey(), entry.getValue());
			}
			rows.add(row);
		}
		return rows;
	}
	
	public Path getFolderPath() {
		String path = "result/" + id;
		return Path.of("result/", id);
	}
}
