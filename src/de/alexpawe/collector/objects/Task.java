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
	
	
	public Task(String id, JSONObject taskObj) {
		this.id = id;
		this.parameters = taskObj.toMap();
		
		addValues();
	}
	
	private void addValues() {
		iterations = new HashMap<Integer, Map<String, String>>();
		
		metricsheader = null;
		List<String[]> r = null;
		
		String path = "results/" + id + "/combined.csv";
		try (CSVReader reader = new CSVReader(new FileReader(path))) {
			r = reader.readAll();
			if (r.size() > 0) {
			metricsheader = r.get(0);
				for (int j = 1; j < r.size(); j++) {
					String[] line = r.get(j);
					Map<String, String> tmp = new HashMap<String, String>();
					for (int i = 0; i < line.length; i++) {	
						tmp.put(metricsheader[i], line[i]);
						//System.out.println(tmp);
					}
					iterations.put(j, tmp);
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
	
	public Set<String> getParameterKeys() {
		return parameters.keySet();
	}
	
	public String[] getMetricsheader() {
		return metricsheader;
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
