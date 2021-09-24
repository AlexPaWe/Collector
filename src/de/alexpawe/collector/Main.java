package de.alexpawe.collector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.json.*;

import de.alexpawe.collector.objects.Table;
import de.alexpawe.collector.objects.Task;

public class Main {

	public static void main(String[] args) {
		
		// Read tasks.json file and create an JSON Object
		System.out.print("Reading tasks.json file...");
		String jsonString = null;
		try {
			jsonString = Files.readString(Paths.get("results\\tasks.json"));
					//readString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(jsonString);
		
		// Collect all the data of every Task id
		String[] taskIDs = obj.getNames(obj);
		
		Map<String, Task> Tasks = new HashMap<String, Task>();
		
		for (String taskID : taskIDs) {
			JSONObject taskObj = obj.getJSONObject(taskID);
			Task task = new Task(taskID, taskObj);
			Tasks.put(taskID, task);
		}
		System.out.println(" done!");
		
		// Get the header
		Set<String> header = new TreeSet<String>();
		header.add("TASKID");
		for (Entry<String, Task> entry : Tasks.entrySet()) {
			for (String pkey : entry.getValue().getParameterKeys()) {
				header.add(pkey);
			}
			String[] metricsheader = entry.getValue().getMetricsheader();
			if (metricsheader != null) {
				for (String metric : metricsheader) {
					header.add(metric);
				}
			}
		}
		header.add("Iteration");
		//System.out.println("Header is " + header.toString());
		
		// Create table
		System.out.print("Create table...");
		Table table = new Table(header);
		for (String taskID : taskIDs) {
			List<Map<String, String>> rows = Tasks.get(taskID).getRows();
			table.addRows(rows);
		}
		
		// Print table
		String tablestring = table.toString();
		//System.out.println(tablestring);
		System.out.println(" done!");
		
		// Export table as csv
		System.out.print("Save table to results.csv file...");
		try (PrintWriter writer = new PrintWriter(new File("results.csv"))) {
			writer.write(tablestring);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" done!");
		
		System.out.println("Done!");
	}

}
